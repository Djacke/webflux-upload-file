package cn.ops.controller;

import cn.ops.model.Person;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by chenjq on 02/05/2018.
 */
@RestController
@RequestMapping("/api")
public class FileUploadController {

  @PostMapping(value = "/upload")
  public Mono<?> upload(@RequestPart("file") FilePart excelFile)
      throws IOException, InvalidFormatException {
    return DataBufferUtils.join(excelFile.content()).flatMap(dataBuffer -> {
      try {
        Sheet sheet = WorkbookFactory.create(dataBuffer.asInputStream()).getSheetAt(0);
        return Mono.just(StreamSupport.stream(sheet.spliterator(), false).map(row -> {
          return StreamSupport.stream(row.spliterator(), false).map(cell -> {
            Object value = null;
            switch (cell.getCellTypeEnum()) {
              case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
              case NUMERIC:
                value = cell.getNumericCellValue();
                break;
              case STRING:
                value = cell.getStringCellValue();
                break;
              default:
                break;
            }
            return value;
          }).collect(Collectors.toList());
        }).collect(Collectors.toList()));
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InvalidFormatException e) {
        e.printStackTrace();
      }
      return Mono.empty();
    }).switchIfEmpty(Mono.empty());
  }

  @PostMapping("/requestPart")
  Mono<String> requestPart(
      @RequestPart FormFieldPart fieldPart,
      @RequestPart("file") FilePart excelFile,
      @RequestPart("fileParts") FilePart fileParts,
      @RequestPart("fileParts") Mono<FilePart> filePartsMono,
      @RequestPart("fileParts") Flux<FilePart> filePartsFlux,
      @RequestPart("jsonPart") Person person,
      @RequestPart("jsonPart") Mono<Person> personMono) {

    System.out.println("file name============>" + excelFile.filename());
    return filePartsMono.flatMap(f -> Mono.just(f.filename()));
  }
}
