package cn.edu.ustb.service;

import cn.edu.ustb.model.stage.Stage;
import cn.edu.ustb.model.transformation.Transformation;

import java.util.*;

public class DAGBuilder {
    public static List<Stage> buildStages(Transformation<?> finalTransformation) {
        TransformationStack stack = new TransformationStack();
        Queue<Transformation<?>> queue = new LinkedList<>();
        Set<Transformation<?>> visited = new HashSet<>();

        // 广度优先遍历算子链
        queue.add(finalTransformation);
        while (!queue.isEmpty()) {
            Transformation<?> current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            // 压栈处理当前算子
            stack.pushTransformation(current);

            // 将上游算子加入队列（逆向解析）
            for (Transformation<?> parent : current.getInputs()) {
                queue.add(parent);
            }
        }

        // 返回阶段列表（需反转顺序）
        List<Stage> stages = stack.buildStages();
        Collections.reverse(stages);
        return stages;
    }
}
