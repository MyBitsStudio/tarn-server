package com.ruse;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ruse.engine.GameEngine;
import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.BackupThread;
import com.ruse.engine.task.impl.LotteryTask;
import com.ruse.engine.task.impl.ServerTimeUpdateTask;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.definitions.NPCDrops;
import com.ruse.model.definitions.WeaponInterfaces;
import com.ruse.net.PipelineFactory;
import com.ruse.net.security.ConnectionHandler;
import com.ruse.security.ServerSecurity;
import com.ruse.security.save.impl.server.defs.NPCDataLoad;
import com.ruse.world.World;
import com.ruse.world.allornothing.DoubleOrNothing;
import com.ruse.world.clip.region.RegionClipping;
import com.ruse.world.content.*;
import com.ruse.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.ruse.world.content.combat.strategy.CombatStrategies;
import com.ruse.world.content.discordbot.AdminCord;
import com.ruse.world.content.discordbot.Bot;
import com.ruse.world.content.discordbot.JavaCord;
import com.ruse.world.content.grandLottery.GrandLottery;
import com.ruse.world.content.grandexchange.GrandExchangeOffers;
import com.ruse.world.content.groupironman.GroupManager;
import com.ruse.world.content.johnachievementsystem.AchievementHandler;
import com.ruse.world.content.polling.PollManager;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.content.tbdminigame.Lobby;
import com.ruse.world.packages.tradingpost.TradingPost;
import com.ruse.world.entity.actor.player.controller.ControllerHandler;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.packages.attendance.DailyResetScheduler;
import com.ruse.world.packages.clans.ClanManager;
import com.ruse.world.packages.donation.DonateSales;
import com.ruse.world.packages.donation.DonationManager;
import com.ruse.world.packages.donation.FlashDeals;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.packages.instances.InstanceManager;
import com.ruse.world.packages.misc.ItemIdentifiers;
import com.ruse.world.packages.plus.PlusUpgrade;
import com.ruse.world.packages.seasonpass.SeasonPassLoader;
import com.ruse.world.packages.shops.ShopHandler;
import com.ruse.world.packages.voting.VoteBossDrop;
import com.ruse.world.region.RegionManager;
import com.server.service.login.ServiceManager;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Credit: lare96, Gabbe
 */
public final class GameLoader {

	private final ExecutorService serviceLoader = Executors
			.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());
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

		World.LOGIN_SERVICE.postLoad();

	}

	private void startInstances(){
		FlashDeals.getDeals();
		DonationManager.getInstance();
		InstanceManager.getManager();
		GlobalBossManager.getInstance();
		PlusUpgrade.getPlusUpgrade();
		DonateSales.getInstance();
		new Lobby();
		ServiceManager.INSTANCE.init();
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
		TaskManager.submit(new BackupThread());

	}

	private void executeServiceLoad() {
		/*
		 * if (GameSettings.MYSQL_ENABLED) { serviceLoader.execute(() ->
		 * MySQLController.init()); }
		 */

		serviceLoader.execute(GroupManager::loadGroups);
		serviceLoader.execute(ConnectionHandler::init);
		serviceLoader.execute(RegionClipping::init);
		serviceLoader.execute(CustomObjects::init);
		serviceLoader.execute(RegionManager::initialise);
		serviceLoader.execute(ControllerHandler::initialise);
		serviceLoader.execute(ItemDefinition::init);
		serviceLoader.execute(WellOfGoodwill::init);
		serviceLoader.execute(ClanManager.getManager()::init);
		serviceLoader.execute(CombatPoisonData::init);
		serviceLoader.execute(CombatStrategies::init);
		serviceLoader.execute(() -> new NPCDataLoad().loadArray("./.core/server/defs/npc/npc_data.json").run());

		serviceLoader.execute(() -> NPCDrops.parseDrops().load());
		serviceLoader.execute(() -> WeaponInterfaces.parseInterfaces().load());
		serviceLoader.execute(WeaponInterfaces::init);
		serviceLoader.execute(NPC::init);
		serviceLoader.execute(ServerPerks.getInstance()::load);
		serviceLoader.execute(Bot::init);
		serviceLoader.execute(JavaCord::init);
		serviceLoader.execute(AdminCord::init);
		serviceLoader.execute(SeasonPassLoader::load);
		serviceLoader.execute(() -> DonationManager.getInstance().load());
		serviceLoader.execute(VoteBossDrop::load);
		serviceLoader.execute(DailyResetScheduler::initialize);
		serviceLoader.execute(TradingPost::load);
		serviceLoader.execute(ItemIdentifiers::load);
		serviceLoader.execute(ShopHandler::load);
		serviceLoader.execute(ShopHandler::loadPrices);
		serviceLoader.execute(AchievementHandler::load);
//		serviceLoader.execute(World.handler.load()::startEvents);
		TaskManager.submit(new LotteryTask());
	}

	public GameEngine getEngine() {
		return engine;
	}
}