package ghana7.perpetualstew.util;

import ghana7.perpetualstew.PerpetualStewMod;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;

import java.util.Arrays;

public class FoodInfo {
    public enum FoodType {Poison, Meat, Veggie, Sweets, Magic}
    private static String[] validFoods = {
            "item.minecraft.apple",
            "item.minecraft.baked_potato",
            "item.minecraft.beef",
            "item.minecraft.beetroot",
            "item.minecraft.beetroot_soup",
            "item.minecraft.bread",
            "item.minecraft.carrot",
            "item.minecraft.chicken",
            "item.minecraft.chorus_fruit",
            "item.minecraft.cod",
            "item.minecraft.cooked_beef",
            "item.minecraft.cooked_chicken",
            "item.minecraft.cooked_cod",
            "item.minecraft.cooked_mutton",
            "item.minecraft.cooked_porkchop",
            "item.minecraft.cooked_rabbit",
            "item.minecraft.cooked_salmon",
            "item.minecraft.cookie",
            "item.minecraft.dried_kelp",
            "item.minecraft.golden_apple",
            "item.minecraft.golden_carrot",
            "item.minecraft.honey_bottle",
            "item.minecraft.melon_slice",
            "item.minecraft.mushroom_stew",
            "item.minecraft.poisonous_potato",
            "item.minecraft.porkchop",
            "item.minecraft.potato",
            "item.minecraft.pufferfish",
            "item.minecraft.pumpkin_pie",
            "item.minecraft.rabbit",
            "item.minecraft.rabbit_stew",
            "item.minecraft.rotten_flesh",
            "item.minecraft.salmon",
            "item.minecraft.spider_eye",
            "item.minecraft.suspicious_stew",
            "item.minecraft.sweet_berries",
            "item.minecraft.tropical_fish"
    };

    public static boolean isValidFood(Item item) {
        return Arrays.stream(validFoods).anyMatch(item.getTranslationKey()::equals);
    }
    public static int getFoodColor(Item item) {
        switch (item.getTranslationKey()) {
            case "item.minecraft.apple":
                return 0xaf1d1f;
            case "item.minecraft.baked_potato":
                return 0xaf812d;
            case "item.minecraft.beef":
                return 0xb73b33;
            case "item.minecraft.beetroot":
                return 0x882b2a;
            case "item.minecraft.beetroot_soup":
                return 0x882b2a;
            case "item.minecraft.bread":
                return 0x8a651d;
            case "item.minecraft.carrot":
                return 0xca6816;
            case "item.minecraft.chicken":
                return 0xcd9d8b;
            case "item.minecraft.chorus_fruit":
                return 0x7e5d7d;
            case "item.minecraft.cod":
                return 0xa6815b;
            case "item.minecraft.cooked_beef":
                return 0x603525;
            case "item.minecraft.cooked_chicken":
                return 0xb1704a;
            case "item.minecraft.cooked_cod":
                return 0xb59877;
            case "item.minecraft.cooked_mutton":
                return 0x744534;
            case "item.minecraft.cooked_porkchop":
                return 0xad9665;
            case "item.minecraft.cooked_rabbit":
                return 0xaa7350;
            case "item.minecraft.cooked_salmon":
                return 0x915331;
            case "item.minecraft.cookie":
                return 0xb57038;
            case "item.minecraft.dried_kelp":
                return 0x312c24;
            case "item.minecraft.golden_apple":
                return 0xead457;
            case "item.minecraft.golden_carrot":
                return 0xead457;
            case "item.minecraft.honey_bottle":
                return 0xe68d1c;
            case "item.minecraft.melon_slice":
                return 0xb12619;
            case "item.minecraft.mushroom_stew":
                return 0xcb8e70;
            case "item.minecraft.poisonous_potato":
                return 0x8c8236;
            case "item.minecraft.porkchop":
                return 0xd17171;
            case "item.minecraft.potato":
                return 0xb28033;
            case "item.minecraft.pufferfish":
                return 0xbe9a52;
            case "item.minecraft.pumpkin_pie":
                return 0xc88c4d;
            case "item.minecraft.rabbit":
                return 0xcfa190;
            case "item.minecraft.rabbit_stew":
                return 0xc26e38;
            case "item.minecraft.rotten_flesh":
                return 0x7b3c1d;
            case "item.minecraft.salmon":
                return 0x81443e;
            case "item.minecraft.spider_eye":
                return 0x611128;
            case "item.minecraft.suspicious_stew":
                return 0xbf9972;
            case "item.minecraft.sweet_berries":
                return 0x761516;
            case "item.minecraft.tropical_fish":
                return 0xc96c3d;
            default:
                return Fluids.WATER.getAttributes().getColor();
        }
    }
    public static FoodType getFoodType(Item item) {
        switch (item.getTranslationKey()) {
            case "item.minecraft.apple":
                return FoodType.Sweets;
            case "item.minecraft.baked_potato":
                return FoodType.Veggie;
            case "item.minecraft.beef":
                return FoodType.Meat;
            case "item.minecraft.beetroot":
                return FoodType.Veggie;
            case "item.minecraft.beetroot_soup":
                return FoodType.Veggie;
            case "item.minecraft.bread":
                return FoodType.Veggie;
            case "item.minecraft.carrot":
                return FoodType.Veggie;
            case "item.minecraft.chicken":
                return FoodType.Meat;
            case "item.minecraft.chorus_fruit":
                return FoodType.Magic;
            case "item.minecraft.cod":
                return FoodType.Meat;
            case "item.minecraft.cooked_beef":
                return FoodType.Meat;
            case "item.minecraft.cooked_chicken":
                return FoodType.Meat;
            case "item.minecraft.cooked_cod":
                return FoodType.Meat;
            case "item.minecraft.cooked_mutton":
                return FoodType.Meat;
            case "item.minecraft.cooked_porkchop":
                return FoodType.Meat;
            case "item.minecraft.cooked_rabbit":
                return FoodType.Meat;
            case "item.minecraft.cooked_salmon":
                return FoodType.Meat;
            case "item.minecraft.cookie":
                return FoodType.Sweets;
            case "item.minecraft.dried_kelp":
                return FoodType.Veggie;
            case "item.minecraft.golden_apple":
                return FoodType.Magic;
            case "item.minecraft.golden_carrot":
                return FoodType.Magic;
            case "item.minecraft.honey_bottle":
                return FoodType.Sweets;
            case "item.minecraft.melon_slice":
                return FoodType.Sweets;
            case "item.minecraft.mushroom_stew":
                return FoodType.Veggie;
            case "item.minecraft.poisonous_potato":
                return FoodType.Poison;
            case "item.minecraft.porkchop":
                return FoodType.Meat;
            case "item.minecraft.potato":
                return FoodType.Veggie;
            case "item.minecraft.pufferfish":
                return FoodType.Poison;
            case "item.minecraft.pumpkin_pie":
                return FoodType.Sweets;
            case "item.minecraft.rabbit":
                return FoodType.Meat;
            case "item.minecraft.rabbit_stew":
                return FoodType.Meat;
            case "item.minecraft.rotten_flesh":
                return FoodType.Poison;
            case "item.minecraft.salmon":
                return FoodType.Meat;
            case "item.minecraft.spider_eye":
                return FoodType.Poison;
            case "item.minecraft.suspicious_stew":
                return FoodType.Veggie;
            case "item.minecraft.sweet_berries":
                return FoodType.Sweets;
            case "item.minecraft.tropical_fish":
                return FoodType.Meat;
            default:
                return FoodType.Veggie;
        }
    }
}
