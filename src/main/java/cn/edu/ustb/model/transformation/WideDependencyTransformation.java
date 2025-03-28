package cn.edu.ustb.model.transformation;

public class WideDependencyTransformation {
    // 在 TransformationStack 类中
    public void pushTransformation(Transformation<?> transformation) {
        // 遇到宽依赖时切割阶段
        if (transformation instanceof WideDependencyTransformation) {
            stages.add(currentStage);       // 将当前阶段存入列表
            currentStage = new Stage();     // 创建新阶段
        }
        operatorStack.push(transformation); // 压入当前算子
        currentStage.addTransformation(transformation); // 加入当前阶段
    }
}
