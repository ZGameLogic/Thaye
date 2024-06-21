package com.zgamelogic.data.database.seaOfThieves;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.zgamelogic.data.discord.SeaOfThievesEventData;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sot_data")
@NoArgsConstructor
@Data
public class SOTDateAvailable {
    @Id
    @GeneratedValue
    private long id;
    private boolean ben;
    private boolean patrick;
    private boolean jj;
    private boolean greg;
    private boolean success;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime proposed;


    public SOTDateAvailable(SeaOfThievesEventData data){
        id = 0;
        ben = data.ben();
        greg = data.greg();
        jj = data.jj();
        patrick = data.patrick();
        success = data.success();
        proposed = parseDateString(data.time());
    }

    private LocalDateTime parseDateString(String dateString) {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(dateString);

        if (groups.isEmpty()) return null; // No dates found

        DateGroup group = groups.get(0);
        List<Date> dates = group.getDates();

        return dates.isEmpty() ? null : dates.get(0).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); // Return the first parsed date
    }
}
