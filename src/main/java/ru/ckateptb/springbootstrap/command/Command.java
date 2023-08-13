package ru.ckateptb.springbootstrap.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public interface Command {
    void register(CommandDispatcher<CommandSourceStack> source);
}
