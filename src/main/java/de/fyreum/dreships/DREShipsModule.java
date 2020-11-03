package de.fyreum.dreships;

import de.fyreum.dreships.commands.DsCommand;
import de.fyreum.dreships.data.ConfigLocalization;
import de.fyreum.dreships.data.LangLocalization;
import de.fyreum.dreships.sign.SignListener;
import de.gleyder.umbrella.core.command.logic.CommandRegistry;
import de.gleyder.umbrella.core.module.ListenerRegistry;
import de.gleyder.umbrella.core.module.UmbrellaModule;
import lombok.NonNull;

public class DREShipsModule extends UmbrellaModule {

    public DREShipsModule() {
        super("dreships", LangLocalization.class, ConfigLocalization.class);
    }

    @Override
    protected void registerCommands(@NonNull CommandRegistry commandRegistry) {
        commandRegistry.register(new DsCommand());
    }

    @Override
    protected void registerListener(@NonNull ListenerRegistry listenerRegistry) {
        listenerRegistry.register(new SignListener());
    }

}
