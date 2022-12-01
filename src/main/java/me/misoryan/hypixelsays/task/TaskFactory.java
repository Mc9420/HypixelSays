package me.misoryan.hypixelsays.task;

import lombok.Getter;
import lombok.SneakyThrows;
import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.util.ClassUtil;

import java.util.*;

/**
 * @Author Misoryan
 * @Date 2022/11/30 17:13
 */
@Getter
public class TaskFactory {

    private final List<AbstractTask> tasks;

    public TaskFactory() {
        this.tasks = new ArrayList<>();
    }

    @SneakyThrows
    public void init() {
        Collection<Class<?>> classes = ClassUtil.getClassesInPackage(HypixelSays.getInstance(),HypixelSays.MAIN_PACKAGE + ".task.type");
        for (Class<?> clazz : classes) {
            if (AbstractTask.class.isAssignableFrom(clazz)) {
                Object instance = clazz.newInstance();
                tasks.add((AbstractTask) instance);
            }
        }
    }
}
