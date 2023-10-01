package com.ruse.model.input.impl;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.input.Input;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.referral.Referrals;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.ruse.world.content.PlayerLogs.getTime;

public class EnterReferral extends Input {

    @Override
    public void handleSyntax(Player player, String syntax) {
        Referrals.getInstance().claimReferral(player, syntax);
    }
}