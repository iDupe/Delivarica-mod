package MainMod.Blocks;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.Styles;
import mindustry.world.blocks.production.GenericCrafter;

public class RecipeCrafter extends GenericCrafter {
    public Seq<CraftRecipe> recipes = new Seq<>();

    public RecipeCrafter(String name) {
        super(name);
        configurable = true;
        saveConfig = true;
        clearOnDoubleTap = true;

        config(Integer.class, (RecipeCrafterBuild build, Integer index) -> {
            if (index >= 0 && index < recipes.size && build.currentRecipe != index) {
                build.currentRecipe = index;
                build.progress = 0f;
            }
        });
    }

    public static class CraftRecipe {
        public ItemStack[] inputItems;
        public LiquidStack inputLiquid;
        public ItemStack outputItem;
        public float craftTime;

        public CraftRecipe(ItemStack[] inputItems, LiquidStack inputLiquid, ItemStack outputItem, float craftTime) {
            this.inputItems = inputItems;
            this.inputLiquid = inputLiquid;
            this.outputItem = outputItem;
            this.craftTime = craftTime;
        }
    }

    @Override
    public void init() {
        // По умолчанию берём первый рецепт для отображения базовых параметров в инфо-карточке | Fallback to first recipe
        if (recipes.any()) {
            CraftRecipe first = recipes.first();
            outputItem = first.outputItem;
            craftTime = first.craftTime;
            if (first.inputLiquid != null) {
                consumeLiquid(first.inputLiquid.liquid, first.inputLiquid.amount);
            }
            if (first.inputItems != null) {
                consumeItems(first.inputItems);
            }
        }
        super.init();
    }

    public class RecipeCrafterBuild extends GenericCrafterBuild {
        public int currentRecipe = 0;

        public CraftRecipe recipe() {
            return recipes.get(Math.min(currentRecipe, recipes.size - 1));
        }

        @Override
        public void buildConfiguration(Table table) {
            table.table(Styles.black6, t -> {
                for (int i = 0; i < recipes.size; i++) {
                    final int idx = i;
                    CraftRecipe r = recipes.get(i);
                    t.button(b -> {
                        b.image(r.outputItem.item.uiIcon).size(24f);
                    }, Styles.clearTogglei, () -> configure(idx))
                    .checked(b -> currentRecipe == idx)
                    .size(40f);
                }
            });
        }

        @Override
        public boolean acceptItem(Building source, mindustry.type.Item item) {
            CraftRecipe r = recipe();
            if (r.inputItems == null) return false;
            for (ItemStack stack : r.inputItems) {
                if (stack.item == item) {
                    return items.get(item) < getMaximumAccepted(item);
                }
            }
            return false;
        }

        @Override
        public boolean acceptLiquid(Building source, mindustry.type.Liquid liquid) {
            CraftRecipe r = recipe();
            return r.inputLiquid != null && r.inputLiquid.liquid == liquid && liquids.get(liquid) < liquidCapacity;
        }

        @Override
        public boolean shouldConsume() {
            CraftRecipe r = recipe();
            if (r.outputItem != null && items.get(r.outputItem.item) + r.outputItem.amount > itemCapacity) {
                return false;
            }
            if (r.inputItems != null) {
                for (ItemStack stack : r.inputItems) {
                    if (items.get(stack.item) < stack.amount) return false;
                }
            }
            if (r.inputLiquid != null) {
                if (liquids.get(r.inputLiquid.liquid) < r.inputLiquid.amount * craftTime) return false;
            }
            return enabled;
        }

        @Override
        public void craft() {
            CraftRecipe r = recipe();
            consume();
            if (r.inputItems != null) {
                for (ItemStack stack : r.inputItems) {
                    items.remove(stack);
                }
            }
            if (r.outputItem != null) {
                for (int i = 0; i < r.outputItem.amount; i++) {
                    offload(r.outputItem.item);
                }
            }
            if (wasVisible) {
                craftEffect.at(x, y);
            }
            progress %= 1f;
        }

        @Override
        public float getProgressIncrease(float baseTime) {
            return super.getProgressIncrease(recipe().craftTime);
        }

        @Override
        public Object config() {
            return currentRecipe;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(currentRecipe);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            currentRecipe = read.i();
        }
    }
}
