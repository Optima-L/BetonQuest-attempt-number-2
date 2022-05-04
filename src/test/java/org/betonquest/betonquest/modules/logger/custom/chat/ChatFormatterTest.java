package org.betonquest.betonquest.modules.logger.custom.chat;

import org.betonquest.betonquest.api.config.QuestPackage;
import org.betonquest.betonquest.modules.logger.BetonQuestLogRecord;
import org.betonquest.betonquest.modules.logger.util.BetonQuestLoggerService;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * A test for the {@link ChatFormatter}.
 */
@ExtendWith(BetonQuestLoggerService.class)
class ChatFormatterTest {
    /**
     * The mocked plugin instance.
     */
    private final Plugin plugin;
    /**
     * The mocked plugin instance from an extension.
     */
    private final Plugin pluginExtension;

    /**
     * Default constructor.
     */
    public ChatFormatterTest() {
        plugin = mock(Plugin.class);
        pluginExtension = mock(Plugin.class);
        when(plugin.getName()).thenReturn("BetonQuest");
        when(pluginExtension.getName()).thenReturn("Extension");
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void testChatFormatting() {
        final BetonQuestLogRecord record = new BetonQuestLogRecord(plugin, null, Level.INFO, "Message1");
        final String expected1 = "{\"text\":\"§fMessage1\"}";
        final String expected2 = "{\"text\":\"§7[§8BQ§7]§r §fMessage1\"}";
        final String expected3 = "{\"text\":\"§7[§8BetonQuest§7]§r §fMessage1\"}";
        assertLogMessage(ChatFormatter.PluginDisplayMethod.NONE, null, null, record, expected1);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.PLUGIN, plugin, "BQ", record, expected2);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.PLUGIN, plugin, null, record, expected3);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN, plugin, "BQ", record, expected2);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN, plugin, null, record, expected3);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN_AND_PLUGIN, plugin, "BQ", record, expected2);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN_AND_PLUGIN, plugin, null, record, expected3);
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void testChatFormattingLogRecord() {
        final LogRecord record = new LogRecord(Level.INFO, "Message2");
        final String expected1 = "{\"text\":\"§fMessage2\"}";
        final String expected2 = "{\"text\":\"§7[§8?§7]§r §fMessage2\"}";
        final String expected3 = "{\"text\":\"§7[§8BQ§7]§r §fMessage2\"}";
        final String expected4 = "{\"text\":\"§7[§8BetonQuest§7]§r §fMessage2\"}";
        final String expected5 = "{\"text\":\"§7[§8BQ | ?§7]§r §fMessage2\"}";
        final String expected6 = "{\"text\":\"§7[§8BetonQuest | ?§7]§r §fMessage2\"}";
        assertLogMessage(ChatFormatter.PluginDisplayMethod.NONE, null, null, record, expected1);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.PLUGIN, plugin, "BQ", record, expected2);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.PLUGIN, plugin, null, record, expected2);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN, plugin, "BQ", record, expected3);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN, plugin, null, record, expected4);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN_AND_PLUGIN, plugin, "BQ", record, expected5);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN_AND_PLUGIN, plugin, null, record, expected6);
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void testChatFormattingPlugin() {
        final BetonQuestLogRecord record = new BetonQuestLogRecord(pluginExtension, null, Level.INFO, "Message3");
        final String expected1 = "{\"text\":\"§fMessage3\"}";
        final String expected2 = "{\"text\":\"§7[§8Extension§7]§r §fMessage3\"}";
        final String expected3 = "{\"text\":\"§7[§8BQ§7]§r §fMessage3\"}";
        final String expected4 = "{\"text\":\"§7[§8BetonQuest§7]§r §fMessage3\"}";
        final String expected5 = "{\"text\":\"§7[§8BQ | Extension§7]§r §fMessage3\"}";
        final String expected6 = "{\"text\":\"§7[§8BetonQuest | Extension§7]§r §fMessage3\"}";
        assertLogMessage(ChatFormatter.PluginDisplayMethod.NONE, null, null, record, expected1);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.PLUGIN, plugin, "BQ", record, expected2);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.PLUGIN, plugin, null, record, expected2);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN, plugin, "BQ", record, expected3);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN, plugin, null, record, expected4);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN_AND_PLUGIN, plugin, "BQ", record, expected5);
        assertLogMessage(ChatFormatter.PluginDisplayMethod.ROOT_PLUGIN_AND_PLUGIN, plugin, null, record, expected6);
    }

    @Test
    void testChatFormattingPackage() {
        final QuestPackage pack = mock(QuestPackage.class);
        when(pack.getPackagePath()).thenReturn("TestPackage");
        final BetonQuestLogRecord record = new BetonQuestLogRecord(plugin, pack, Level.INFO, "Message3");
        final String expected = "{\"text\":\"\\u003cTestPackage\\u003e §fMessage3\"}";
        assertLogMessage(ChatFormatter.PluginDisplayMethod.NONE, null, null, record, expected);
    }

    @Test
    void testChatFormattingException() {
        final BetonQuestLogRecord record = new BetonQuestLogRecord(plugin, null, Level.INFO, "Message4");
        record.setThrown(new NullPointerException("Exception Message"));
        final String message = getFormattedMessage(ChatFormatter.PluginDisplayMethod.NONE, null, null, record);
        final String start = "{\"extra\":[{\"color\":\"red\",\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\"\\n"
                + "java.lang.NullPointerException: Exception Message\\n\\";
        final String end = "}},\"text\":\" Hover for Stacktrace!\"}],\"text\":\"§fMessage4\"}";
        assertEquals(start, message.substring(0, start.length()), "The start of the log message is not correct formatted");
        assertEquals(end, message.substring(message.length() - end.length()), "The end of the log message is not correct formatted");
    }

    private void assertLogMessage(final ChatFormatter.PluginDisplayMethod displayMethod, final Plugin plugin,
                                  final String shortName, final LogRecord record, final String expected) {
        final String formatted = getFormattedMessage(displayMethod, plugin, shortName, record);
        assertEquals(expected, formatted, "Message is not correct formatted");
    }

    private String getFormattedMessage(final ChatFormatter.PluginDisplayMethod displayMethod, final Plugin plugin,
                                       final String shortName, final LogRecord record) {
        final ChatFormatter formatter = new ChatFormatter(displayMethod, plugin, shortName);
        return formatter.format(record).replace("\\r\\n", "\\n").replace("\\r", "\\n");
    }
}
