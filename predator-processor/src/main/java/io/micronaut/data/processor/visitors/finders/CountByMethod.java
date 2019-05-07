package io.micronaut.data.processor.visitors.finders;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.data.intercept.CountAllInterceptor;
import io.micronaut.data.intercept.CountByInterceptor;
import io.micronaut.data.model.query.Query;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.ast.MethodElement;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class CountByMethod extends AbstractFindByFinder {

    private static final String METHOD_PATTERN = "((count)(\\S*?)By)([A-Z]\\w*)";

    public CountByMethod() {
        super(Pattern.compile(METHOD_PATTERN));
    }

    @Nullable
    @Override
    protected PredatorMethodInfo buildInfo(@NonNull MethodMatchContext matchContext, ClassElement queryResultType, @Nullable Query query) {
        if (query != null) {
            query.projections().count();
            return new PredatorMethodInfo(
                    matchContext.getReturnType(),
                    query,
                    CountByInterceptor.class
            );
        } else {
            return new PredatorMethodInfo(
                    matchContext.getReturnType(),
                    null,
                    CountAllInterceptor.class
            );
        }
    }

    @Override
    public boolean isMethodMatch(MethodElement methodElement) {
        return super.isMethodMatch(methodElement) && TypeUtils.doesReturnNumber(methodElement);
    }
}
