/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.integtests.resolve

import groovy.transform.NotYetImplemented
import org.gradle.integtests.fixtures.AbstractIntegrationSpec

class DirectoryOutputArtifactIntegrationTest extends AbstractIntegrationSpec {

    @NotYetImplemented
    def "can attach a directory as output of a configuration"() {
        given:
        buildFile << '''

        configurations {
            compile
        }

        artifacts {
            compile file("someDir")
        }

        task check {
            doLast {
                println configurations.compile.files as List
                assert configurations.compile.files.name == ['someDir']
            }
        }
        '''
        file('someDir/a.txt') << 'some text'

        when:
        run 'check'

        then:
        noExceptionThrown()
    }

    @NotYetImplemented
    def "can attach a directory as output of a configuration generated by another task"() {
        given:
        buildFile << '''

        configurations {
            compile
        }

       task generateFiles {
            ext.outputDir = file("$buildDir/someDir")
            doLast {
                ext.outputDir.mkdirs()
                file("${ext.outputDir}/a.txt") << 'some text'
            }
        }

        artifacts {
            compile file:file("$buildDir/someDir"), builtBy: generateFiles
        }

        task check {
            doLast {
                println configurations.compile.files as List
                assert configurations.compile.files.name == ['someDir']
            }
        }

        task run(dependsOn: configurations.compile) {
            doLast {
                assert configurations.compile.files*.listFiles().flatten().text == ['some text']
            }
        }

        '''

        when:
        run 'check'

        then:
        notExecuted ':generateFiles'

        when:
        run 'run'

        then:
        executedAndNotSkipped ':generateFiles'
    }

    def "can attach a directory as output of a configuration generated by another task in a different project"() {
        given:
        file('settings.gradle') << "include 'a', 'b'"
        file('a/build.gradle') << '''

        configurations {
            compile
        }

        dependencies {
            compile project(path: ':b', configuration: 'compile')
        }

        task check {
            doLast {
                println configurations.compile.files as List
                assert configurations.compile.files.name == ['someDir']
            }
        }

        task run(dependsOn: configurations.compile) {
            doLast {
                assert configurations.compile.files*.listFiles().flatten().text == ['some text']
            }
        }
        '''

        file('b/build.gradle') << '''

        configurations {
            compile
        }

        task generateFiles {
            ext.outputDir = file("$buildDir/someDir")
            doLast {
                ext.outputDir.mkdirs()
                file("${ext.outputDir}/a.txt") << 'some text'
            }
        }

        artifacts {
            compile file:file("$buildDir/someDir"), builtBy: generateFiles
        }
        '''

        when:
        run 'a:check'

        then:
        notExecuted ':b:generateFiles'

        when:
        run 'a:run'

        then:
        executedAndNotSkipped ':b:generateFiles'
    }
}
