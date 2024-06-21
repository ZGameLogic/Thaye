package com.zgamelogic.controllers;

import com.zgamelogic.annotations.DiscordController;
import com.zgamelogic.annotations.DiscordMapping;
import com.zgamelogic.annotations.EventProperty;
import com.zgamelogic.data.database.seaOfThieves.SOTDateAvailable;
import com.zgamelogic.data.database.seaOfThieves.SOTRepository;
import com.zgamelogic.data.discord.SeaOfThievesEventData;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.springframework.context.annotation.Bean;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@DiscordController
@Slf4j
public class SeaOfThievesBot {
    private final static Color SEA_OF_THIEVES_COLOR = new Color(21, 230, 154);
    private final SOTRepository sotRepository;

    public SeaOfThievesBot(SOTRepository sotRepository) {
        this.sotRepository = sotRepository;
    }

    @DiscordMapping(Id = "sot", SubId = "data-point")
    private void addData(
            SlashCommandInteractionEvent event,
            @EventProperty SeaOfThievesEventData data
    ){
        SOTDateAvailable returnData = sotRepository.save(new SOTDateAvailable(data));
        event.replyEmbeds(sotDataMessage(returnData)).queue();
    }

    private MessageEmbed sotDataMessage(SOTDateAvailable returnData) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(SEA_OF_THIEVES_COLOR);
        eb.setTitle("Data recorded");
        eb.appendDescription(String.format("Time for event: <t:%d:F>\n", returnData.getProposed().atZone(ZoneId.systemDefault()).toEpochSecond()));
        eb.appendDescription(String.format("\nBen: `%b`\nGreg: `%b`\nJJ: `%b`\nPatrick `%b`\n", returnData.isBen(), returnData.isGreg(), returnData.isJj(), returnData.isPatrick()));
        eb.appendDescription(String.format("Success: `%b`", returnData.isSuccess()));
        eb.setFooter("ID: " + returnData.getId());
        eb.setTimestamp(Instant.now());
        return eb.build();
    }

    @Bean
    private List<CommandData> slashCommands(){
        return List.of(
                Commands.slash("sot", "Slash Commands for sea of thieves").addSubcommands(
                        new SubcommandData("data-point", "Record a datapoint to the database")
                                .addOption(OptionType.BOOLEAN, "ben", "Did Ben join", true)
                                .addOption(OptionType.BOOLEAN, "greg", "Did Greg join", true)
                                .addOption(OptionType.BOOLEAN, "jj", "Did JJ join", true)
                                .addOption(OptionType.BOOLEAN, "patrick", "Did Patrick join", true)
                                .addOption(OptionType.BOOLEAN, "success", "Did this event happen", true)
                                .addOption(OptionType.STRING, "time", "What time did this take place", true)
                )
        );
    }
}
