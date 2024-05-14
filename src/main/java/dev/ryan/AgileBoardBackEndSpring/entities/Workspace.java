package dev.ryan.AgileBoardBackEndSpring.entities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "workspaces")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private WorkspaceIcon icon;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "workspaces")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Board> boards = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @JsonSerialize(using = WorkspaceIcon.Serializer.class)
    public enum WorkspaceIcon {
        LAYOUT("Layout"),
        USERS("Users"),
        CALENDAR("Calendar"),
        SETTINGS("Settings"),
        BRIEFCASE("Briefcase"),
        HOME("Home"),
        STAR("Star"),
        CHECK_SQUARE("CheckSquare"),
        FILE_TEXT("FileText"),
        BAR_CHART_2("BarChart2"),
        MESSAGE_SQUARE("MessageSquare"),
        CLOCK("Clock"),
        MAP_PIN("MapPin"),
        ARCHIVE("Archive"),
        FEATHER("Feather"),
        TAG("Tag"),
        DATABASE("Database"),
        CAMERA("Camera");

        private final String icon;

        WorkspaceIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }

        @JsonSerialize(using = Serializer.class)
        static class Serializer extends StdSerializer<WorkspaceIcon> {
            public Serializer() {
                super(WorkspaceIcon.class);
            }

            @Override
            public void serialize(WorkspaceIcon value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.getIcon());
            }
        }
    }

}