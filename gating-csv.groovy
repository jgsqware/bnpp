@Grab('com.xlson.groovycsv:groovycsv:1.1')
import com.xlson.groovycsv.CsvParser
import java.nio.file.Paths

def cli = new CliBuilder(usage: 'gating-csv.groovy -[f] <module> <environment>')
cli.with {
    h longOpt: 'help', 'Show usage information'
    f longOpt: 'gating-file', args: 1, argName: 'file.csv', 'Specifiy an external file. Default: "resources/gating.csv"'
}

def options = cli.parse(args)
if (!options) {
    System.exit(2)
}
if (options.h) {
    cli.usage() 
    System.exit(2)
}

gatingFile = 'resources/gating.csv'
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

Paths.get(gatingFile).withReader { reader ->
    for (line in new CsvParser().parse(reader)) {        
        if(line.module.contains(module) && line.environment.equals(environment)){
            if(Boolean.valueOf(line.deployable)){
                println "Yes"
                System.exit(0)
            }else {
                println "No"
                System.exit(1)
            }
        }
    }
}
println "\"$module\" on \"$environment\" not found"
System.exit(1)



