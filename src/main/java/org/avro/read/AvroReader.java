package org.avro.read;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.avro.read.EvolutionTools.*;

public class AvroReader {

 private static final Logger logger = LoggerFactory.getLogger(AvroReader.class);
  public static void main(String... args)     throws Throwable{

   checkCompatibility("v4", "v3");
   checkCompatibility("v5", "v4");
   checkCompatibility("v4", "v5");
  }


}
