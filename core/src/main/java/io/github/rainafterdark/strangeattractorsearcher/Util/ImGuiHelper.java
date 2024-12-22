package io.github.rainafterdark.strangeattractorsearcher.Util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import imgui.ImGui;
import imgui.flag.ImGuiColorEditFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImInt;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ImGuiHelper {
    public static void tooltip(String text) {
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.text(text);
            ImGui.endTooltip();
        }
    }

    public static void helpMarker(String hint) {
        ImGui.textDisabled("(?)");
        tooltip(hint);
    }

    public static void detachableTab(String label, Runnable renderFunction) {
        int tabFlags = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.Framed;
        if (ImGui.treeNodeEx(label + " ", tabFlags)) {
            renderFunction.run();
            ImGui.treePop();
        }
    }

    public static boolean intWidget(String label, String hint, Supplier<Integer> getter, Consumer<Integer> setter, float vSpeed, int min, int max) {
        boolean changed = false;
        int currentValue = getter.get();
        int[] buffer = { currentValue };
        if (ImGui.dragInt(label, buffer, vSpeed, min, max)) {
            setter.accept(MathUtils.clamp(buffer[0], min, max));
            ConfigSingleton.getInstance().saveToFile();
            changed = true;
        }
        ImGui.sameLine();
        helpMarker(hint);
        return changed;
    }

    public static boolean floatWidget(String label, String hint, Supplier<Float> getter, Consumer<Float> setter, float vSpeed, float min, float max, String format) {
        boolean changed = false;
        float currentValue = getter.get();
        float[] buffer = { currentValue };
        if (ImGui.dragFloat(label, buffer, vSpeed, min, max, format)) {
            setter.accept(MathUtils.clamp(buffer[0], min, max));
            ConfigSingleton.getInstance().saveToFile();
            changed = true;
        }
        ImGui.sameLine();
        helpMarker(hint);
        return changed;
    }

    public static boolean enumWidget(String label, String hint, Class<? extends Enum<?>> enumClass, Supplier<Enum<?>> getter, Consumer<Enum<?>> setter) {
        boolean changed = false;
        String[] items = Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toArray(String[]::new);
        ImInt currentItem = new ImInt(getter.get().ordinal());
        if (ImGui.combo(label, currentItem, items)) {
            setter.accept(enumClass.getEnumConstants()[currentItem.get()]);
            ConfigSingleton.getInstance().saveToFile();
            changed = true;
        }
        ImGui.sameLine();
        helpMarker(hint);
        return changed;
    }

    public static boolean listWidget(String label, String hint, List<?> items, Supplier<Integer> getter, Consumer<Integer> setter) {
        boolean changed = false;
        boolean empty = items.isEmpty();
        String[] itemNames = items.stream().map(Object::toString).toArray(String[]::new);
        for (int i = 0; i < itemNames.length; i++) {
            itemNames[i] = String.format("[%02d] %s", i, itemNames[i]);
        }
        if (empty) itemNames = new String[] { "<Empty>" };
        ImInt currentItem = new ImInt(empty ? 0 : getter.get());
        if (empty) ImGui.beginDisabled();
        if (ImGui.button("x##Delete" + label) && !empty) {
            items.remove(currentItem.get());
            if (items.isEmpty()) {
                setter.accept(0);
            } else {
                setter.accept(Math.max(0, currentItem.get() - 1));
            }
            changed = true;
        }
        ImGui.sameLine();
        if (ImGui.combo(label, currentItem, itemNames)) {
            setter.accept(currentItem.get());
            changed = true;
        }
        ImGui.sameLine();
        helpMarker(hint);
        ImGui.sameLine();
        if (ImGui.button("<##Prev" + label) && !empty) {
            int newIndex = currentItem.get() - 1;
            if (newIndex < 0) newIndex = items.size() - 1;
            setter.accept(newIndex);
            changed = true;
        }
        ImGui.sameLine();
        if (ImGui.button(">##Next" + label) && !empty) {
            int newIndex = currentItem.get() + 1;
            if (newIndex >= items.size()) newIndex = 0;
            setter.accept(newIndex);
            changed = true;
        }
        if (empty) ImGui.endDisabled();
        if (changed) ConfigSingleton.getInstance().saveToFile();
        return changed;
    }

    public static boolean gradientWidget(String label, Supplier<Gradient> getter, Consumer<Gradient> setter) {
        AtomicBoolean changed = new AtomicBoolean(false);
        detachableTab(label, () -> {
            Gradient currentValue = getter.get();
            List<Color> colors = currentValue.getColors();

            if (ImGui.button("Rainbow")) {
                setter.accept(Gradient.getRainbowGradient());
                changed.set(true);
            }
            ImGui.sameLine();
            if (ImGui.button("Randomize")) {
                for (Color color : colors) {
                    color.set(ColorHelper.getRandom());
                }
                changed.set(true);
            }
            ImGui.sameLine();
            if (ImGui.button("Clear All")) {
                colors.clear();
                colors.add(ColorHelper.getRandom());
                changed.set(true);
            }

            int toMoveUp = -1, toMoveDown = -1,
                toAdd = -1, toRemove = -1,
                toRandomize = -1;

            for (int i = 0; i < colors.size(); i++) {
                Color color = colors.get(i);
                float[] buffer = { color.r, color.g, color.b };
                if (ImGui.colorEdit3((i + 1) + "##Color" + i, buffer, ImGuiColorEditFlags.DisplayHSV)) {
                    colors.set(i, new Color(buffer[0], buffer[1], buffer[2], 1f));
                    changed.set(true);
                }

                ImGui.sameLine();
                if (ImGui.button("^##MoveUp" + i)) toMoveUp = i;
                ImGui.sameLine();
                if (ImGui.button("v##MoveDown" + i)) toMoveDown = i;
                ImGui.sameLine();
                if (ImGui.button("+##AddColor" + i)) toAdd = i;
                ImGui.sameLine();
                if (ImGui.button("-##RemoveColor" + i)) toRemove = i;
                ImGui.sameLine();
                if (ImGui.button("?##Randomize" + i)) toRandomize = i;
            }

            if (toMoveUp != -1) {
                if (toMoveUp > 0) {
                    Color temp = colors.get(toMoveUp);
                    colors.set(toMoveUp, colors.get(toMoveUp - 1));
                    colors.set(toMoveUp - 1, temp);
                    changed.set(true);
                }
            }

            if (toMoveDown != -1) {
                if (toMoveDown < colors.size() - 1) {
                    Color temp = colors.get(toMoveDown);
                    colors.set(toMoveDown, colors.get(toMoveDown + 1));
                    colors.set(toMoveDown + 1, temp);
                    changed.set(true);
                }
            }

            if (toAdd != -1) {
                colors.add(toAdd + 1, new Color(colors.get(toAdd)));
                changed.set(true);
            }

            if (toRemove != -1) {
                colors.remove(toRemove);
                changed.set(true);
            }

            if (toRandomize != -1) {
                colors.set(toRandomize, ColorHelper.getRandom());
                changed.set(true);
            }
        });

        if (changed.get()) {
            ConfigSingleton.getInstance().saveToFile();
        }
        return changed.get();
    }
}
