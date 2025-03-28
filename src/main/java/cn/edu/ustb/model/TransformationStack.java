package cn.edu.ustb.model;

import cn.edu.ustb.model.stage.Stage;
import cn.edu.ustb.model.transformation.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TransformationStack {
    private Stack<Transformation<?>> operatorStack = new Stack<>();
    private List<Stage> stages = new ArrayList<>();
    private Stage currentStage = new Stage("stage-1");

    public void pushTransformation(Transformation<?> transformation) {
        // 遇到宽依赖时切割阶段
        if (transformation instanceof WideDependencyTransformation) {
            stages.add(currentStage);       // 将当前阶段存入列表
            currentStage = new Stage();     // 创建新阶段
        }
        operatorStack.push(transformation); // 压入当前算子
        currentStage.addTransformation(transformation); // 加入当前阶段
    }

    public List<Stage> buildStages() {
        stages.add(currentStage); // 添加最后一个阶段
        return stages;
    }
}
