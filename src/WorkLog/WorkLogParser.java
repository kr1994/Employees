package WorkLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WorkLogParser {

    public static List<WorkLogDto> readFile(File file) {
        try (
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader)
        ) {
            return extractWorkLogDtoList(br);
        } catch (IOException ioException) {
            throw new RuntimeException("File wasn't opened!", ioException);
        }
    }

    private static List<WorkLogDto> extractWorkLogDtoList(BufferedReader br) throws IOException {
        // extract information from the txt file
        String line;
        List<WorkLogDto> result = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            WorkLogDto employeeDto = null;
            try {
                employeeDto = extractWorkLogDto(line);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            result.add(employeeDto);
        }
        return result;
    }

    private static WorkLogDto extractWorkLogDto(String line) throws ParseException {
        // split the information and fill it in a WorkLogDto object
        String[] strLine = line.split(", ");
        int employeeId = Integer.parseInt(strLine[0]);
        int projectId = Integer.parseInt(strLine[1]);
        Date dateFrom = convertDateFormat(strLine[2]);
        Date dateTo = extractDateTo(strLine[3]);
        return new WorkLogDto(employeeId, projectId, dateFrom, dateTo);
    }

    private static Date extractDateTo(String strDate) {
        // change NULL to NOW()
        return (!strDate.equals("NULL"))
                ? convertDateFormat(strDate) : new Date();
    }

    public static Date convertDateFormat(String dateValue)
    {
        /*
         * Set the permissible formats.
         */
        String[] formats = new String[]{ "MM/dd/yyyy hh:mm:ss.sss",
                                         "MM/dd/yyyy hh:mm:ss",
                                         "yyyy-MM-dd hh:mm:ss.sss",
                                         "yyyy-MM-dd hh:mm",
                                         "dd/MM/yyyy",
                                         "dd-MM-yyyy",
                                         "MM-dd-yyyy",
                                         "ddMMyyyy",
                                         "dd-MM-yy",
                                         "yyyy-MM-dd" };
        /*
         * Loop through array of formats and validate using JAVA API.
         */

        int i;
        for (i = 0; i < formats.length; i++)
        {
            // try to parse the date, if successfully, return it, if not, try the next permissible format.
            try
            {
                SimpleDateFormat sdfObj = new SimpleDateFormat(formats[i]);
                sdfObj.setLenient(false);
                Date date = sdfObj.parse(dateValue);
                System.out.println("Looks like a valid date for Date Value : " + dateValue + ": For Format: "
                        + formats[i]);
                return date;
            }
            catch (ParseException e)
            {
                System.out.println("Parse Exception Occurred for Date Value :" + dateValue + ": And Format:"
                        + formats[i]);
            }
        }
        return null;
    }

}
