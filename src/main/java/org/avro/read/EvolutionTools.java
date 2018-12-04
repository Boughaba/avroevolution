package org.avro.read;

import fr.fonctiere.schema.person.Person;
import org.apache.avro.Schema;
import org.apache.avro.SchemaCompatibility;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by BOUGHABA on 30/11/18.
 */
public class EvolutionTools {
    private  static final Logger logger = LoggerFactory.getLogger(EvolutionTools.class);
    private static  final String basePath = "src/main/avro/Person";
    public static void readPersonAvro(String version) throws Throwable {


        String flux = "person";
        String avroFile = flux + version+".avro";
        DatumReader<Person> datumReader = new SpecificDatumReader<>(Person.class);
        File fileName = new File(avroFile);
        DataFileReader<Person> dataFileReader = new DataFileReader<Person>(
                fileName,
                datumReader);
        Schema writerSchema = dataFileReader.getSchema();
        System.out.println(" ===================== Check compatibility  =========================================== \n\n");

        Schema readerSchema = Person.SCHEMA$;
        SchemaCompatibility.SchemaPairCompatibility compatibilityChecker = SchemaCompatibility.checkReaderWriterCompatibility(readerSchema, writerSchema);
        System.out.println("\t\t\t\t\tcompatibility check is : " + compatibilityChecker.getType().toString());
        System.out.println(" ================================= Print Avro schema file  ============================================== \n\n");


        System.out.println(writerSchema.toString(true));


        System.out.println(" ================================= Print  data file  ============================================== \n\n\n");
        Person record = null;
        int i = 0;
        while (dataFileReader.hasNext()) {
            record = dataFileReader.next(record);
            System.out.println("Record  : " + record.toString());
            break;
        }


    }

    public static void writeAvro(String version) throws Throwable {

        Person person = createPerson();
        DatumWriter<Person> datumWriter = new SpecificDatumWriter<>(Person.class);

        DataFileWriter<Person> empFileWriter = new DataFileWriter<Person>(datumWriter);

        empFileWriter.create(person.getSchema(), new File("person"+version+".avro"));

        empFileWriter.append(person);
        empFileWriter.close();
    }


    private static Person createPerson() {
         return Person.newBuilder()
                .setFirstName("Marc")
                .setSecondName("Jacque")
                .setId(1).build();
    }


    public static void checkCompatibility(String readerVersion, String writerVersion) throws IOException {
        Schema.Parser parserreader = new Schema.Parser();
        Schema.Parser parserwriter= new Schema.Parser();
        Schema readerSchema = parserreader.parse(new File(basePath+readerVersion +".avsc"));
        Schema writerSchema = parserwriter.parse(new File(basePath+writerVersion+".avsc"));
        SchemaCompatibility.SchemaPairCompatibility compatibilityChecker =  SchemaCompatibility.checkReaderWriterCompatibility(readerSchema, writerSchema);
        logger.info("compatibility check is : " + compatibilityChecker.getType().toString());
    }

}
