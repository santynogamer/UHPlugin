/*
 * Copyright or © or Copr. Amaury Carrade (2014 - 2016)
 *
 * http://amaury.carrade.eu
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package eu.carrade.amaury.UHCReloaded.commands.commands.uh.timers;

import eu.carrade.amaury.UHCReloaded.UHCReloaded;
import eu.carrade.amaury.UHCReloaded.commands.core.AbstractCommand;
import eu.carrade.amaury.UHCReloaded.commands.core.annotations.Command;
import eu.carrade.amaury.UHCReloaded.commands.core.exceptions.CannotExecuteCommandException;
import eu.carrade.amaury.UHCReloaded.commands.core.utils.CommandUtils;
import eu.carrade.amaury.UHCReloaded.timers.UHTimer;
import eu.carrade.amaury.UHCReloaded.utils.UHUtils;
import fr.zcraft.zlib.components.i18n.I;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Command (name = "list")
public class UHTimersListCommand extends AbstractCommand
{
    private UHCReloaded p;

    public UHTimersListCommand(UHCReloaded p)
    {
        this.p = p;
    }

    @Override
    public void run(CommandSender sender, String[] args) throws CannotExecuteCommandException
    {
        Collection<UHTimer> timers = p.getTimerManager().getTimers();

        sender.sendMessage(I.tn("{ci}{0} timer is registered.", "{ci}{0} timers are registered.", timers.size()));

        for (UHTimer timer : timers)
        {
            if (timer.isRunning())
            {
                if (timer.isPaused())
                {
                    sender.sendMessage(I.tn("{yellow} • {{ci}{0}{ci} - total {1} second - {2}", "{yellow} • {{ci}{0}{ci} - total {1} seconds - {2}",
                            timer.getDuration(),
                            timer.getDisplayName(),
                            timer.getDuration(),
                            timer.toString()
                    ));
                }
                else
                {
                    sender.sendMessage(I.tn("{green} • {ci}{0}{ci} - total {1} second - {2}", "{green} • {ci}{0}{ci} - total {1} seconds - {2}",
                            timer.getDuration(),
                            timer.getDisplayName(),
                            timer.getDuration(),
                            timer.toString()
                    ));
                }
            }
            else
            {
                sender.sendMessage(I.tn("{red} • {ci}{0}{ci} - total {1} second", "{red} • {ci}{0}{ci} - total {1} seconds",
                        timer.getDuration(),
                        timer.getDisplayName(),
                        timer.getDuration()
                ));
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args)
    {
        List<String> suggestions = new ArrayList<>();

        for (UHTimer timer : p.getTimerManager().getTimers())
        {
            suggestions.add(timer.getName());
        }

        return CommandUtils.getAutocompleteSuggestions(UHUtils.getStringFromCommandArguments(args, 0), suggestions, args.length - 1);
    }

    @Override
    public List<String> help(CommandSender sender)
    {
        return null;
    }

    @Override
    public List<String> onListHelp(CommandSender sender)
    {
        return Collections.singletonList(I.t("{cc}/uh timers list {ci}: lists the registered timers."));
    }
}
