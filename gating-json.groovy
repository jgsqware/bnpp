@Grab(group='org.slf4j', module='slf4j-simple', version='1.6.1')
@Grab(group='com.jayway.jsonpath', module='json-path', version='2.2.0')
import static com.jayway.jsonpath.JsonPath.parse
import java.nio.file.Paths

def cli = new CliBuilder(usage: 'gating-json.groovy -[f] <module> <environment>')
cli.with {
    h longOpt: 'help', 'Show usage information'
    f longOpt: 'gating-file', args: 1, argName: 'file.json', 'Specifiy an external file. Default: "resources/gating.json"'
}

def options = cli.parse(args)
if (!options) {
    System.exit(2)
}
if (options.h) {
    cli.usage() 
    System.exit(2)
}

gatingFile = 'resources/gating.json'
if (options.'gating-file'){
    println "using file: "+ options.f
    gatingFile = options.f
}

if(args.size() < 2){
    println "Missing <module> | <environment> arguments"
    System.exit(2)
}

def module = options.arguments()[0].toUpperCase()
def environment = options.arguments()[1].toUpperCase()


println "Is " + module + " deployable on " + environment + " ?"

def module_env = parse(Paths.get(gatingFile).text).read('$..module[?(@.name == "'+module+'")].environment[?(@.name == "'+environment+'")]')
if(module_env.size() >0){
    if(module_env[0].deployable){
        println "Yes"
        System.exit(0)
    }else {
        println "No"
        System.exit(1)
    }
}

println "\"$module\" on \"$environment\" not found"
System.exit(1)