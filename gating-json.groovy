import groovy.json.JsonSlurper
import java.nio.file.Paths

if(args.size() != 2) {
    println "2 arguments needed"
    exit 1
}

def jsonSlurper = new JsonSlurper()
def gating

Paths.get('resources/gating.json').withReader { reader ->
    gating = jsonSlurper.parse(reader)
}

for(environment in gating.environments) {
    println "$environment.name"
}