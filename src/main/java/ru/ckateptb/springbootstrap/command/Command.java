package ru.ckateptb.springbootstrap.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public interface Command {
    void register(CommandDispatcher<CommandSource> source);
}
