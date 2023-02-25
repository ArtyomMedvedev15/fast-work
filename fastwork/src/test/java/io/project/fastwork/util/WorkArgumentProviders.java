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
                                    .workName("Test work")
                            .workDescribe("Fast work Fast work")
                                    .workPrice(14.00f)
                                    .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Loader in the warehouse")
                            .workDescribe("Fast work Fast work")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Loader in the cinema")
                            .workDescribe("Fast work Fast work")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Builder")
                            .workDescribe("Fast work Fast work")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build())
                    );
        }
    }

    static public class WorkArgumentInvalidProviders implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of(Work.builder()
                            .workName("test ")
                            .workDescribe("testwor kdescribe")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("      ")
                            .workDescribe("Fast work Fast work")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("1231231")
                            .workDescribe("Fast work Fast work")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Bui")
                            .workDescribe("Fast work Fast work")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Builder123Builder123Builder123Builder123Builder123Builder123Builder123Builder123Builder123")
                            .workDescribe("Fast work Fast work")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Bui")
                            .workDescribe("Fast work Fast work")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("№!?*((())")
                            .workDescribe("Fast work Fast work")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Bui ")
                            .workDescribe("Fast work Fast work")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Builder")
                            .workDescribe("Fast1 ")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Builder")
                            .workDescribe("         ")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Builder")
                            .workDescribe("asdas    asdasdasd")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Builder")
                            .workDescribe("asd")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Builder")
                            .workDescribe("№№;%%%;")
                            .workPrice(14.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Builder")
                            .workDescribe("Fast work Fast work")
                            .workPrice(0.00f)
                            .workCountPerson(3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Builder")
                            .workDescribe("Fast work Fast work")
                            .workPrice(0.00f)
                            .workCountPerson(-3)
                            .build()),
                    Arguments.of(Work.builder()
                            .workName("Builder")
                            .workDescribe("Fast work Fast work")
                            .workPrice(0.00f)
                            .workCountPerson(16)
                            .build())
            );
        }
    }

}
