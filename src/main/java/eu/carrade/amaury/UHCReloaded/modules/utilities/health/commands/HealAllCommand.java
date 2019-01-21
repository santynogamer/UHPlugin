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
package eu.carrade.amaury.UHCReloaded.modules.utilities.health.commands;

import eu.carrade.amaury.UHCReloaded.modules.core.teams.TeamsModule;
import eu.carrade.amaury.UHCReloaded.shortcuts.UR;
import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


@CommandInfo (name = "heal-all", usageParameters = "[half-hearts=20|±diff]", aliases = {"healall"})
public class HealAllCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        double diffHealth;
        boolean add = false; // "add" (±, true) or "raw" (exact health, false) mode

        // /uh heal-all: full life for player.
        if (args.length == 0)
        {
            diffHealth = Integer.MAX_VALUE;
        }

        // /uh heal-all <hearts>
        else
        {
            try
            {
                if (args[0].startsWith("+") || args[0].startsWith("-"))
                {
                    add = true;
                }

                diffHealth = Double.parseDouble(args[0]);
            }
            catch (final NumberFormatException e)
            {
                throwInvalidArgument(I.t("{ce}Hey, this is not a number of half-hearts. It's a text. Pfff."));
                return;
            }
        }

        if (!add && diffHealth <= 0)
        {
            error(I.t("{ce}Serial killer!"));
        }

        for (final Player player : Bukkit.getServer().getOnlinePlayers())
        {
            double health = !add ? diffHealth : player.getHealth() + diffHealth;

            if (health <= 0D)
            {
                warning(I.t("{ce}The health of {0} was not updated to avoid a kill.", player.getName()));
                continue;
            }
            else if (health > player.getMaxHealth())
            {
                health = player.getMaxHealth();
            }

            player.setHealth(health);
            UR.module(TeamsModule.class).getSidebarPlayerCache(player.getUniqueId()).updateHealth(health);
        }

        if (!add)
        {
            if (diffHealth == Integer.MAX_VALUE)
            {
                success(I.t("The health of all players was completely filled.", diffHealth));
            }
            else
            {
                success(I.t("The health of all players was set to {0}.", diffHealth));
            }
        }
        else if (diffHealth > 0)
        {
            success(I.t("The health of all players was increased by {0}.", diffHealth));
        }
        else
        {
            success(I.t("The health of all players was decreased by {0}.", -diffHealth));
        }
    }
}