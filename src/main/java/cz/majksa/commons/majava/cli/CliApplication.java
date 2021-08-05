/*
 *  majava - cz.majksa.commons.majava.cli.CliController
 *  Copyright (C) 2021  Majksa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cz.majksa.commons.majava.cli;

import cz.majksa.commons.majava.application.Application;
import cz.majksa.commons.majava.application.commands.Debug;
import cz.majksa.commons.majava.application.commands.Stop;
import cz.majksa.commons.majava.application.commands.Restart;
import cz.majksa.commons.majava.application.commands.Start;
import cz.majksa.commons.majava.application.commands.Status;
import cz.majksa.commons.majava.cli.commands.CliRunner;
import cz.majksa.commons.majava.cli.commands.CommandsGroup;
import cz.majksa.commons.majava.cli.commands.ConsoleRuntimeException;
import cz.majksa.commons.majava.logging.LoggingModule;
import cz.majksa.commons.majava.utils.StringUtils;

import javax.annotation.Nonnull;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p><b>Class {@link CliApplication}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class CliApplication {

    public static final String HEADING = "Welcome to Majava";

    @Nonnull
    private final Application application;

    @Nonnull
    private final CliRunner cliRunner = new CliRunner();

    @Nonnull
    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    @Nonnull
    private final ConsoleTextBuilder consoleMessenger = new ConsoleTextBuilder();

    @Nonnull
    private final Thread hook;
    /**
     * If application has been started
     */
    private boolean running = false;

    /**
     * Constructor
     *
     * @param application the application
     */
    public CliApplication(@Nonnull Application application) {
        this.application = application;
        hook = new Thread(() -> {
            if (application.isRunning()) {
                application.stop();
            }
            if (!executor.isShutdown()) {
                executor.shutdown();
            }
        });
        final CommandsGroup group = cliRunner.register("application", "application core commands");
        group
                .register(new Start(group, application, this))
                .register(new Status(group, application))
                .register(new Debug(group, application))
                .register(new Restart(group, application))
                .register(new Stop(group, application));
        printWelcome();
    }

    /**
     * Starts the cli application listener for commands
     */
    public void listen() {
        running = true;
        final Scanner scanner = new Scanner(System.in);
        Runtime.getRuntime().addShutdownHook(hook);
        executor.execute(() -> {
            while (running) {
                String command = scanner.nextLine();
                application.getLogger().atDebug().log("Executing: {}", command);
                application.getModules().get(LoggingModule.class).run(() -> run(command.split(" ")));
            }
        });
    }

    /**
     * Runs a command line input
     *
     * @param args the command line input
     */
    public void run(@Nonnull String[] args) {
        try {
            if (args.length == 0 || (args.length == 1 && args[0].equals(""))) {
                cliRunner.run("help");
            } else {
                cliRunner.run(args);
            }
        } catch (Throwable throwable) {
            if (throwable instanceof ConsoleRuntimeException) {
                consoleMessenger
                        .modify(ConsoleModifiers.RED)
                        .append(throwable.getMessage())
                        .print();
                return;
            }
            throw throwable;
        }
    }

    /**
     * Prints a welcome message
     */
    private void printWelcome() {
        final int headingSpace = ConsoleTextBuilder.WIDTH - HEADING.length();
        consoleMessenger
                .modify(ConsoleModifiers.BOLD)
                .modify(ConsoleModifiers.BLUE_BACKGROUND)
                .modify(ConsoleModifiers.BLACK)
                .append(StringUtils.repeat(" ", headingSpace / 2 + headingSpace % 2))
                .append(HEADING)
                .append(StringUtils.repeat(" ", headingSpace / 2))
                .print();
    }

}
