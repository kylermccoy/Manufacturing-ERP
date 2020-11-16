package com.kennuware.erp.manufacturing.service;

import com.kennuware.erp.manufacturing.service.controller.QueueController;

import com.kennuware.erp.manufacturing.service.model.Employee;
import com.kennuware.erp.manufacturing.service.model.Item;
import com.kennuware.erp.manufacturing.service.model.Product;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.Recipe;
import com.kennuware.erp.manufacturing.service.model.RecipeComponent;
import com.kennuware.erp.manufacturing.service.model.repository.EmployeeRepository;
import com.kennuware.erp.manufacturing.service.model.repository.ItemRepository;
import com.kennuware.erp.manufacturing.service.model.repository.ProductRepository;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import com.kennuware.erp.manufacturing.service.model.repository.RecipeRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(QueueRepository repository, EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, ProductRepository productRepository, RecipeRepository recipeRepository, ItemRepository itemRepository) {
    return args -> {
      Employee e = new Employee();
      e.setUsername("admin");
      e.setPassword(passwordEncoder.encode("admin"));
      if (!employeeRepository.existsByUsername("admin")) {
        log.info("Preloading " + employeeRepository.save(e));
      }
      Queue q = new Queue();
      q.setName(QueueController.QUEUE_NAME);
      q.setRunning(false);
      q.setRequestsInQueue(Collections.emptyList());
      if (!repository.existsByName(QueueController.QUEUE_NAME)) {
        log.info("Preloading " + repository.save(q));
      }

      Item usbInputItem = new Item("USB Input");
      usbInputItem.setId((long)0);
      Item glassScreenItem = new Item("Glass Screen");
      glassScreenItem.setId((long)1);
      Item wristbandItem = new Item("Wristband");
      wristbandItem.setId((long)2);
      Item motherboardItem = new Item("Motherboard");
      motherboardItem.setId((long)3);
      Item bioSensorItem = new Item("Bio-sensor");
      bioSensorItem.setId((long)4);
      Item polyesterFabricItem = new Item("Polyester Fabric");
      polyesterFabricItem.setId((long)5);
      Item VelcroItem = new Item("Velcro");
      VelcroItem.setId((long)6);
      Item heatingPadItem = new Item("Heating Pad");
      heatingPadItem.setId((long)7);
      Item watchScreenItem = new Item("Watch Screen");
      watchScreenItem.setId((long)8);
      Item jacketItem = new Item("Jacket");
      jacketItem.setId((long)9);
      Item cottonFabricItem = new Item("Cotton Fabric");
      cottonFabricItem.setId((long)10);
      Item buttonItem = new Item("Button");
      buttonItem.setId((long)11);
      Item zipperItem = new Item("Zipper");
      zipperItem.setId((long)12);
      Item ledLightsItem = new Item("LED Lights");
      ledLightsItem.setId((long)13);
      Item lacesItem = new Item("Laces");
      lacesItem.setId((long)14);
      Item bluetoothConnectorItem = new Item("Bluetooth Connector");
      bluetoothConnectorItem.setId((long)15);
      Item smallBatteryItem = new Item("Small Battery");
      smallBatteryItem.setId((long)16);
      Item largeBatteryItem = new Item("Large Battery");
      largeBatteryItem.setId((long)17);
      Item syntheticLeatherItem = new Item("Synthetic Leather");
      syntheticLeatherItem.setId((long)18);
      Item shoeSoleItem = new Item("Shoe Sole");
      shoeSoleItem.setId((long)19);
      Item laceTightenerItem = new Item("Lace Tightener");
      laceTightenerItem.setId((long)20);
      Item glassFramesItem = new Item("Glass Frames");
      glassFramesItem.setId((long)21);
      Item miniProjectorItem = new Item("Mini Projector");
      miniProjectorItem.setId((long)22);
      Item microchipItem = new Item("Microchip");
      microchipItem.setId((long)23);
      Item aluminumBandItem = new Item("Aluminum Band");
      aluminumBandItem.setId((long)24);
      Item motorItem = new Item("Motor");
      motorItem.setId((long)25);
      Item heelysItem = new Item("Heelys");
      heelysItem.setId((long)26);

      List<Item> items = Stream
          .of(usbInputItem, glassScreenItem, wristbandItem, motherboardItem, bioSensorItem,
              polyesterFabricItem,
              VelcroItem, heatingPadItem, watchScreenItem, jacketItem, cottonFabricItem, buttonItem,
              zipperItem, ledLightsItem,
              lacesItem, bluetoothConnectorItem, smallBatteryItem, largeBatteryItem,
              syntheticLeatherItem, shoeSoleItem,
              laceTightenerItem, glassFramesItem, miniProjectorItem, microchipItem,
              aluminumBandItem, motorItem, heelysItem)
          .filter(i -> !itemRepository.existsByName(i.getName()))
          .collect(Collectors.toList());
      log.info("Preloading Items: " +
          itemRepository.saveAll(items)
              .stream()
              .map(Item::getName)
              .collect(Collectors.toList()));

      Recipe smartWatchRecipe = new Recipe();
      smartWatchRecipe.setName("Smart Watch Recipe");
      smartWatchRecipe.setBuildTime(30);
      List<String> smartWatchInstructions = new ArrayList<>();
      smartWatchInstructions.add("Add watch screen to assembly line");
      smartWatchInstructions.add("Attach wristband to the watch screen");
      RecipeComponent sw_comp1 = new RecipeComponent(watchScreenItem, 1);
      RecipeComponent sw_comp2 = new RecipeComponent(wristbandItem, 1);
      List<RecipeComponent> smartWatchRecipeComps = Arrays.asList(sw_comp1, sw_comp2);
      smartWatchRecipe.setComponents(smartWatchRecipeComps);
      smartWatchRecipe.setBuildInstructions(smartWatchInstructions);
      smartWatchRecipe.setId((long)0);

      Recipe smartHeadbandRecipe = new Recipe();
      smartHeadbandRecipe.setName("Smart Headband Recipe");
      smartHeadbandRecipe.setBuildTime(110);
      List<String> smartHeadbandInstructions = new ArrayList<>();
      smartHeadbandInstructions.add("Cut polyester fabric to headband size");
      smartHeadbandInstructions.add("Attach velcro to fabric so that can be tied around head");
      smartHeadbandInstructions.add("Attach bio-sensor to fabric to measure biometrics");
      smartHeadbandInstructions.add("Attach small battery near bio-sensor and wire together");
      smartHeadbandInstructions.add("Attach USB input to battery to charge and bio-sensor to upload data");
      RecipeComponent shb_comp1 = new RecipeComponent(polyesterFabricItem, 75);
      RecipeComponent shb_comp2 = new RecipeComponent(bioSensorItem, 1);
      RecipeComponent shb_comp3 = new RecipeComponent(usbInputItem, 1);
      RecipeComponent shb_comp4 = new RecipeComponent(VelcroItem, 2);
      RecipeComponent shb_comp5 = new RecipeComponent(smallBatteryItem, 1);
      List<RecipeComponent> smartHeadbandRecipeComps = Arrays.asList(shb_comp1, shb_comp2, shb_comp3, shb_comp4, shb_comp5);
      smartHeadbandRecipe.setComponents(smartHeadbandRecipeComps);
      smartHeadbandRecipe.setBuildInstructions(smartHeadbandInstructions);
      smartHeadbandRecipe.setId((long)1);

      Recipe lightUpBootsRecipe = new Recipe();
      lightUpBootsRecipe.setName("Light Up Boots Recipe");
      lightUpBootsRecipe.setBuildTime(90);
      List<String> lightUpBootsInstructions = new ArrayList<>();
      lightUpBootsInstructions.add("Place shoe soles to be worked on");
      lightUpBootsInstructions.add("Cut leather to shoe size and attach to soles");
      lightUpBootsInstructions.add("Insert lights to soles and leather");
      lightUpBootsInstructions.add("Insert battery to back of soles and wire to LED lights");
      lightUpBootsInstructions.add("Attach USB input near battery insert and wire to battery");
      lightUpBootsInstructions.add("Lace up the boots through the leather");
      RecipeComponent lub_comp1 = new RecipeComponent(shoeSoleItem, 2);
      RecipeComponent lub_comp2 = new RecipeComponent(syntheticLeatherItem, 40);
      RecipeComponent lub_comp3 = new RecipeComponent(ledLightsItem, 8);
      RecipeComponent lub_comp4 = new RecipeComponent(smallBatteryItem, 2);
      RecipeComponent lub_comp5 = new RecipeComponent(usbInputItem, 2);
      RecipeComponent lub_comp6 = new RecipeComponent(lacesItem, 2);
      List<RecipeComponent> lubComps = Arrays.asList(lub_comp1, lub_comp2, lub_comp3, lub_comp4, lub_comp5, lub_comp6);
      lightUpBootsRecipe.setComponents(lubComps);
      lightUpBootsRecipe.setBuildInstructions(lightUpBootsInstructions);
      lightUpBootsRecipe.setId((long)2);

      Recipe autoHeatingJacketRecipe = new Recipe();
      autoHeatingJacketRecipe.setName("Auto-Heating Jacket Recipe");
      autoHeatingJacketRecipe.setBuildTime(80);
      List<String> autoHeatingJacketInstructions = new ArrayList<>();
      autoHeatingJacketInstructions.add("Lay out jacket and create insert slots for heating pads");
      autoHeatingJacketInstructions.add("Insert heating pads in arm sleeves and down the back");
      autoHeatingJacketInstructions.add("Insert the library near the lower back and wire to heating pads");
      autoHeatingJacketInstructions.add("Attach USB input to battery and wire for charging purposes");
      RecipeComponent ahj_comp1 = new RecipeComponent(jacketItem, 1);
      RecipeComponent ahj_comp2 = new RecipeComponent(heatingPadItem, 3);
      RecipeComponent ahj_comp3 = new RecipeComponent(largeBatteryItem, 1);
      RecipeComponent ahj_comp4 = new RecipeComponent(usbInputItem, 1);
      List<RecipeComponent> ahjComps = Arrays.asList(ahj_comp1, ahj_comp2, ahj_comp3, ahj_comp4);
      autoHeatingJacketRecipe.setComponents(ahjComps);
      autoHeatingJacketRecipe.setBuildInstructions(autoHeatingJacketInstructions);
      autoHeatingJacketRecipe.setId((long)3);

      Recipe tieThemselvesSneakersRecipe = new Recipe();
      tieThemselvesSneakersRecipe.setName("Tie-Themselves Sneakers Recipe");
      tieThemselvesSneakersRecipe.setBuildTime(130);
      List<String> tieThemselvesSneakersInstructions = new ArrayList<>();
      tieThemselvesSneakersInstructions.add("Place shoe soles to be worked on");
      tieThemselvesSneakersInstructions.add("Cut fabric to fit shoe size and attach to soles");
      tieThemselvesSneakersInstructions.add("Insert lace tightener to toe area");
      tieThemselvesSneakersInstructions.add("Attach small battery to back of sole and wire to lace tightener");
      tieThemselvesSneakersInstructions.add("Attach USB input to battery and wire for charging purposes ");
      tieThemselvesSneakersInstructions.add("Lace up the boots through the fabric and through the lace tightener");
      RecipeComponent tss_comp1 = new RecipeComponent(shoeSoleItem, 12);
      RecipeComponent tss_comp2 = new RecipeComponent(polyesterFabricItem, 40);
      RecipeComponent tss_comp3 = new RecipeComponent(laceTightenerItem, 2);
      RecipeComponent tss_comp4 = new RecipeComponent(smallBatteryItem, 2);
      RecipeComponent tss_comp5 = new RecipeComponent(usbInputItem, 2);
      RecipeComponent tss_comp6 = new RecipeComponent(lacesItem, 2);
      List<RecipeComponent> tssComps = Arrays.asList(tss_comp1, tss_comp2, tss_comp3, tss_comp4, tss_comp5, tss_comp6);
      tieThemselvesSneakersRecipe.setComponents(tssComps);
      tieThemselvesSneakersRecipe.setBuildInstructions(tieThemselvesSneakersInstructions);
      tieThemselvesSneakersRecipe.setId((long)4);

      Recipe smartGlassesRecipe = new Recipe();
      smartGlassesRecipe.setName("Smart Glasses Recipe");
      smartGlassesRecipe.setBuildTime(150);
      List<String> smartGlassesInstructions = new ArrayList<>();
      smartGlassesInstructions.add("Place glass frames on assembly line and create insert slots");
      smartGlassesInstructions.add("Attach mini projector to side of frame");
      smartGlassesInstructions.add("Insert microchip along side of frame and wire to mini projector");
      smartGlassesInstructions.add("Attach battery to back of glasses and wire to mini projector");
      smartGlassesInstructions.add("Attach USB input on battery and wire for charging purposes");
      RecipeComponent sg_comp1 = new RecipeComponent(glassFramesItem, 1);
      RecipeComponent sg_comp2 = new RecipeComponent(miniProjectorItem, 2);
      RecipeComponent sg_comp3 = new RecipeComponent(microchipItem, 1);
      RecipeComponent sg_comp4 = new RecipeComponent(smallBatteryItem, 1);
      RecipeComponent sg_comp5 = new RecipeComponent(usbInputItem, 1);
      List<RecipeComponent> sgComps = Arrays.asList(sg_comp1, sg_comp2, sg_comp3, sg_comp4, sg_comp5);
      smartGlassesRecipe.setComponents(sgComps);
      smartGlassesRecipe.setBuildInstructions(smartGlassesInstructions);
      smartGlassesRecipe.setId((long)5);

      Recipe smartRingRecipe = new Recipe();
      smartRingRecipe.setName("Smart Ring Recipe");
      smartRingRecipe.setBuildTime(115);
      List<String> smartRingInstructions = new ArrayList<>();
      smartRingInstructions.add("Shape aluminum band to ring size and create inserts");
      smartRingInstructions.add("Insert bio-sensor to top of ring");
      smartRingInstructions.add("Insert microchip to side of ring and wire to bio-sensor");
      smartRingInstructions.add("Attach USB input to bottom of ring and attach to microchip to upload data");
      RecipeComponent sr_comp1 = new RecipeComponent(aluminumBandItem, 1);
      RecipeComponent sr_comp2 = new RecipeComponent(bioSensorItem, 1);
      RecipeComponent sr_comp3 = new RecipeComponent(microchipItem, 1);
      RecipeComponent sr_comp4 = new RecipeComponent(usbInputItem, 1);
      List<RecipeComponent> srComps = Arrays.asList(sr_comp1, sr_comp2, sr_comp3, sr_comp4);
      smartRingRecipe.setComponents(srComps);
      smartRingRecipe.setBuildInstructions(smartRingInstructions);
      smartRingRecipe.setId((long)6);

      Recipe smartHeelysRecipe = new Recipe();
      smartHeelysRecipe.setName("Smart Heelys Recipe");
      smartHeelysRecipe.setBuildTime(80);
      List<String> smartHeelysInstructions = new ArrayList<>();
      smartHeelysInstructions.add("Dissassemble heelys and create inserts for motor and battery");
      smartHeelysInstructions.add("Add bluetooth connector to back of heelys");
      smartHeelysInstructions.add("Insert motor to wheel slot and link to bluetooth connector");
      smartHeelysInstructions.add("Insert microchip to top of sole and wire to motor");
      smartHeelysInstructions.add("Insert batteries next to motor and wire to motor");
      smartHeelysInstructions.add("Attach USB input at the back of heel and wire to battery and microchip");
      RecipeComponent sh_comp1 = new RecipeComponent(heelysItem, 2);
      RecipeComponent sh_comp2 = new RecipeComponent(bluetoothConnectorItem, 2);
      RecipeComponent sh_comp3 = new RecipeComponent(motorItem, 2);
      RecipeComponent sh_comp4 = new RecipeComponent(microchipItem, 1);
      RecipeComponent sh_comp5 = new RecipeComponent(smallBatteryItem, 2);
      RecipeComponent sh_comp6 = new RecipeComponent(usbInputItem, 2);
      List<RecipeComponent> shComps = Arrays.asList(sh_comp1, sh_comp2, sh_comp3, sh_comp4, sh_comp5, sh_comp6);
      smartHeelysRecipe.setComponents(shComps);
      smartHeelysRecipe.setBuildInstructions(smartHeelysInstructions);
      smartHeelysRecipe.setId((long)7);

      Recipe watchScreenRecipe = new Recipe();
      watchScreenRecipe.setName("Watch Screen Recipe");
      watchScreenRecipe.setBuildTime(50);
      List<String> watchScreenInstructions = new ArrayList<>();
      watchScreenInstructions.add("Layout watch motherboard and create wiring nodes");
      watchScreenInstructions.add("Attach bio-sensor to bottom of watch and wire to motherboard");
      watchScreenInstructions.add("Attach small battery to top of motherboard and wire to motherboard");
      watchScreenInstructions.add("Attach USB input to side of the watch and wire to motherboard");
      RecipeComponent ws_comp1 = new RecipeComponent(motherboardItem, 1);
      RecipeComponent ws_comp2 = new RecipeComponent(bioSensorItem, 1);
      RecipeComponent ws_comp3 = new RecipeComponent(smallBatteryItem, 2);
      RecipeComponent ws_comp4 = new RecipeComponent(usbInputItem, 1);
      List<RecipeComponent> wsComps = Arrays.asList(ws_comp1, ws_comp2, ws_comp3, ws_comp4);
      watchScreenRecipe.setComponents(wsComps);
      watchScreenRecipe.setBuildInstructions(watchScreenInstructions);
      watchScreenRecipe.setId((long)8);

      Recipe jacketRecipe = new Recipe();
      jacketRecipe.setName("Jacket Recipe");
      jacketRecipe.setBuildTime(20);
      List<String> jacketInstructions = new ArrayList<>();
      jacketInstructions.add("Create jacket cut out of the cotton fabric and cut button and zipper");
      jacketInstructions.add("Attach buttons to pockets and sleeves");
      jacketInstructions.add("Attach zipper to front of jacket");
      RecipeComponent j_comp1 = new RecipeComponent(cottonFabricItem, 100);
      RecipeComponent j_comp2 = new RecipeComponent(buttonItem, 10);
      RecipeComponent j_comp3 = new RecipeComponent(zipperItem, 20);
      List<RecipeComponent> jComps = Arrays.asList(j_comp1, j_comp2, j_comp3);
      jacketRecipe.setComponents(jComps);
      jacketRecipe.setBuildInstructions(jacketInstructions);
      jacketRecipe.setId((long)9);

      List<Recipe> recipeList = Stream
          .of(jacketRecipe, autoHeatingJacketRecipe, lightUpBootsRecipe, smartGlassesRecipe,
              smartHeadbandRecipe, smartHeelysRecipe, smartRingRecipe, smartWatchRecipe,
              tieThemselvesSneakersRecipe, watchScreenRecipe)
          .filter(r -> !recipeRepository.existsByName(r.getName()))
          .collect(Collectors.toList());
      log.info("Preloading Recipes: " +
          recipeRepository.saveAll(recipeList)
              .stream()
              .map(Recipe::getName)
              .collect(Collectors.toList()));

      Product smartWatch = new Product();
      smartWatch.setName("Smart Watch");
      smartWatch.setRecipe(smartWatchRecipe);
      smartWatch.setId((long)0);

      Product smartHeadband = new Product();
      smartHeadband.setName("Smart Headband");
      smartHeadband.setRecipe(smartHeadbandRecipe);
      smartHeadband.setId((long)1);

      Product lightUpBoots = new Product();
      lightUpBoots.setName("Light-Up Boots");
      lightUpBoots.setRecipe(lightUpBootsRecipe);
      lightUpBoots.setId((long)2);

      Product autoHeatingJacket = new Product();
      autoHeatingJacket.setName("Auto-Heating Jacket");
      autoHeatingJacket.setRecipe(autoHeatingJacketRecipe);
      autoHeatingJacket.setId((long)3);

      Product tieThemselvesSneakers = new Product();
      tieThemselvesSneakers.setName("Tie-Themselves Sneakers");
      tieThemselvesSneakers.setRecipe(tieThemselvesSneakersRecipe);
      tieThemselvesSneakers.setId((long)4);

      Product smartGlasses = new Product();
      smartGlasses.setName("Smart Glasses");
      smartGlasses.setRecipe(smartGlassesRecipe);
      smartGlasses.setId((long)5);

      Product smartRing = new Product();
      smartRing.setName("Smart Ring");
      smartRing.setRecipe(smartRingRecipe);
      smartRing.setId((long)6);

      Product smartHeelys = new Product();
      smartHeelys.setName("Smart Heelys");
      smartHeelys.setRecipe(smartHeelysRecipe);
      smartHeelys.setId((long)7);

      Product watchScreen = new Product();
      watchScreen.setName("Watch Screen");
      watchScreen.setRecipe(watchScreenRecipe);
      watchScreen.setId((long)8);

      Product jacket = new Product();
      jacket.setName("Jacket");
      jacket.setRecipe(jacketRecipe);
      jacket.setId((long)9);

      List<Product> productList = Stream
          .of(smartWatch, smartHeadband,
              lightUpBoots, autoHeatingJacket, tieThemselvesSneakers, smartGlasses, smartRing,
              smartHeelys, watchScreen, jacket)
          .filter(p -> !productRepository.existsByName(p.getName()))
          .collect(Collectors.toList());

      log.info("Preloading Products: " +
          productRepository.saveAll(productList)
              .stream()
              .map(Product::getName)
              .collect(Collectors.toList()));
    };
  }

}
