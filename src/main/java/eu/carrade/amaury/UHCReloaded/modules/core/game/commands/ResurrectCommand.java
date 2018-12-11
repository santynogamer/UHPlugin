/*
 * Plugin UHCReloaded : Alliances
 *
 * Copyright ou © ou Copr. Amaury Carrade (2016)
 * Idées et réflexions : Alexandre Prokopowicz, Amaury Carrade, "Vayan".
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement,
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 */
package eu.carrade.amaury.UHCReloaded.modules.core.game.commands;

import eu.carrade.amaury.UHCReloaded.modules.core.game.GameModule;
import eu.carrade.amaury.UHCReloaded.shortcuts.UR;
import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@CommandInfo (name = "resurrect", usageParameters = "<player>", aliases = {"revive", "undead"})
public class ResurrectCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        if (args.length != 1)
        {
            throwInvalidArgument(I.t("You must specify the player to resurrect."));
        }

        final OfflinePlayer player = Arrays.stream(Bukkit.getOfflinePlayers())
            .filter(pl -> pl.getName().equalsIgnoreCase(args[0]))
            .findAny().orElse(null);

        if (player == null)
        {
            error(I.t("{ce}This player was never seen on this server."));
        }
        else if (UR.module(GameModule.class).resurrect(player))
        {
            success(I.t("{0} was resurrected.", player.getName()));
        }
        else
        {
            error(I.t("{ce}This player is not playing or dead!"));
        }
    }

    @Override
    protected List<String> complete()
    {
        final GameModule game = UR.module(GameModule.class);

        if (args.length == 1)
        {
            return getMatchingPlayerNames(
                    Bukkit.getOnlinePlayers().stream().filter(player -> !game.isAlive(player)).collect(Collectors.toList()),
                    args[0]
            );
        }

        else return null;
    }
}