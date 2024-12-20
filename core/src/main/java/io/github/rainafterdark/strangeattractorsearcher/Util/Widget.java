package io.github.rainafterdark.strangeattractorsearcher.Util;

import imgui.ImGui;
import imgui.type.ImInt;
import io.github.rainafterdark.strangeattractorsearcher.Data.ConfigSingleton;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Widget {
    public static void helpMarker(String hint) {
        ImGui.textDisabled("(?)");
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.text(hint);
            ImGui.endTooltip();
        }
    }

    public static boolean intWidget(String label, String hint, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int step) {
        boolean changed = false;
        int currentValue = getter.get();
        int[] buffer = { currentValue };
        if (ImGui.sliderInt(label, buffer, min, max)) {
            setter.accept(buffer[0]);
            ConfigSingleton.getInstance().saveToFile();
            changed = true;
        }
        ImGui.sameLine();
        helpMarker(hint);
        return changed;
    }

    public static boolean floatWidget(String label, String hint, Supplier<Float> getter, Consumer<Float> setter, float min, float max, float step) {
        boolean changed = false;
        float currentValue = getter.get();
        float[] buffer = { currentValue };
        if (ImGui.sliderFloat(label, buffer, min, max)) {
            setter.accept(buffer[0]);
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
}
