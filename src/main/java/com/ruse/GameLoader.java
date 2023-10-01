package com.ruse;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ruse.engine.GameEngine;
import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.ServerTimeUpdateTask;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.definitions.WeaponInterfaces;
import com.ruse.net.PipelineFactory;
import com.ruse.security.ServerSecurity;
import com.ruse.security.save.impl.server.ReferralsLoad;
import com.ruse.security.save.impl.server.WellsLoad;
import com.ruse.security.save.impl.server.defs.NPCDropsLoad;
import com.ruse.security.save.impl.server.defs.TablesLoad;
import com.ruse.security.save.impl.server.defs.NPCDataLoad;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.World;
import com.ruse.world.WorldCalendar;
import com.ruse.world.WorldIPChecker;
import com.ruse.world.clip.region.RegionClipping;
import com.ruse.world.content.*;
import com.ruse.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.ruse.world.content.combat.strategy.CombatStrategies;
import com.ruse.world.content.groupironman.GroupManager;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import com.ruse.world.packages.referral.Referrals;
import com.ruse.world.packages.serverperks.ServerPerks;
import com.ruse.world.content.tbdminigame.Lobby;
import com.ruse.world.packages.tradingpost.TradingPost;
import com.ruse.world.entity.actor.player.controller.ControllerHandler;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.packages.clans.ClanManager;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.packages.instances.InstanceManager;
import com.ruse.world.packages.misc.ItemIdentifiers;
import com.ruse.world.packages.seasonpass.SeasonPassLoader;
import com.ruse.world.packages.shops.ShopHandler;
import com.ruse.world.region.RegionManager;
import com.server.service.login.ServiceManager;
import lombok.Getter;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Credit: Corrupt
 */
public final class GameLoader {

	private final ExecutorService serviceLoader = Executors
			.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());
	@Getter
	private final GameEngine engine;
	private final int port;

	GameLoader(int port) {
		this.port = port;
		this.engine = new GameEngine();
	}

	public void init() {
		Preconditions.checkState(!serviceLoader.isShutdown(), "The bootstrap has been bound already!");

		ServerSecurity.getInstance();

		executeServiceLoad();
		serviceLoader.shutdown();

		startInstances();

		postLoad();

		World.LOGIN_SERVICE.postLoad();

	}

	private void startInstances(){
		InstanceManager.getManager();
		GlobalBossManager.getInstance();
		WorldCalendar.getInstance();
		Referrals.getInstance();
		new Lobby();
		ServiceManager.INSTANCE.init();
		WorldIPChecker.getInstance();
	}

	private void postLoad(){
		new WellsLoad().loadJSON(SecurityUtils.WELLS).run();
		WorldCalendar.getInstance().load();
		World.attributes.load();
		WorldIPChecker.getInstance().load();
		new ReferralsLoad().loadJSON(SecurityUtils.REFERRALS).run();
	}

	public void finish() throws IOException, InterruptedException {
		if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES))
			throw new IllegalStateException("The background service load took too long!");
		final ExecutorService networkExecutor = Executors.newCachedThreadPool();
		final ServerBootstrap serverBootstrap = new ServerBootstrap (new NioServerSocketChannelFactory(networkExecutor, networkExecutor));
		serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(port));
		engine.init();
		TaskManager.submit(new ServerTimeUpdateTask());
	}

	private void executeServiceLoad() {

		serviceLoader.execute(GroupManager::loadGroups);
		//serviceLoader.execute(ConnectionHandler::init);
		serviceLoader.execute(RegionClipping::init);
		serviceLoader.execute(CustomObjects::init);
		serviceLoader.execute(RegionManager::initialise);
		serviceLoader.execute(ControllerHandler::initialise);
		serviceLoader.execute(ItemDefinition::init);
		serviceLoader.execute(WellOfGoodwill::init);
		serviceLoader.execute(ClanManager.getManager()::init);
		serviceLoader.execute(CombatPoisonData::init);
		serviceLoader.execute(CombatStrategies::init);
		serviceLoader.execute(World.handler.load()::startEvents);
		serviceLoader.execute(() -> new NPCDataLoad().loadArray("./.core/server/defs/npc/npc_data.json").run());

		//serviceLoader.execute(() -> NPCDrops.parseDrops().load());
		serviceLoader.execute(() -> new TablesLoad().loadArray("./.core/server/defs/drops/tables.json").run());
		serviceLoader.execute(() -> new NPCDropsLoad().loadArray("./.core/server/defs/drops/npcDrops.json").run());

		serviceLoader.execute(() -> WeaponInterfaces.parseInterfaces().load());
		serviceLoader.execute(WeaponInterfaces::init);
		serviceLoader.execute(NPC::init);
		serviceLoader.execute(ServerPerks.getInstance()::load);
		//serviceLoader.execute(Bot::init);
		//serviceLoader.execute(JavaCord::init);
		//serviceLoader.execute(BotManager.getInstance()::init);
		serviceLoader.execute(SeasonPassLoader::load);
		serviceLoader.execute(WorldCalendar::initialize);
		serviceLoader.execute(TradingPost::load);
		serviceLoader.execute(ItemIdentifiers::load);
		serviceLoader.execute(ShopHandler::load);
		serviceLoader.execute(ShopHandler::loadPrices);
		serviceLoader.execute(AchievementHandler::load);

		//TaskManager.submit(new LotteryTask());
	}

}