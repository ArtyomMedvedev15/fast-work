package io.project.fastwork.util;

import io.project.fastwork.domains.Users;
import io.project.fastwork.domains.Work;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class WorkArgumentProviders {

    static public class WorkArgumentValidProviders implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of(Work.builder()
                                    .workName("testwork")
                                    .workDescribe("testworkdescribe")
                                    .workPrice(14.00f)
                                    .workCountPerson(3)
                            .build())
                    );
        }
    }

    static public class WorkArgumentInvalidProviders implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return null;
        }
    }

}
