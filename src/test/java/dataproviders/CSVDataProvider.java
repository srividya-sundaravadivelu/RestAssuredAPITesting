package dataproviders;

import org.testng.annotations.DataProvider;

import utils.CSVReaderUtil;
import utils.ConfigReader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVDataProvider {
	
	
	@DataProvider(name = "getUsersData")
	public Object[][] getGetUsersData() {
	    return filterDataByRequestName("GetUsers");
	}

    @DataProvider(name = "createNewUserData")
    public Object[][] getCreateNewUserData() {
        return filterDataByRequestName("CreateNewUser");
    }

    @DataProvider(name = "updateUserData")
    public Object[][] getUpdateUserData() {
        return filterDataByRequestName("UpdateUser");
    }

    @DataProvider(name = "getUserByIDData")
    public Object[][] getGetUserByIDData() {
        return filterDataByRequestName("GetUserByID");
    }
    
    @DataProvider(name = "getUserByFirstNameData")
    public Object[][] getGetUserByFirstNameData() {
        return filterDataByRequestName("GetUserByFirstName");
    }

    
    @DataProvider(name = "deleteUserByIDData")
    public Object[][] getDeleteUserByIDData() {
        return filterDataByRequestName("DeleteUserByID");
    }
    
    @DataProvider(name = "deleteUserByFirstNameData")
    public Object[][] getDeleteUserByFirstNameData() {
        return filterDataByRequestName("DeleteUserByFirstName");
    }
    // Method to filter data based on request name
    private Object[][] filterDataByRequestName(String requestName) {
        List<Map<String, String>> allData = CSVReaderUtil.readCSVData(ConfigReader.getCsvFile());

        // Filter data by request name
        List<Map<String, String>> filteredData = allData.stream()
                .filter(row -> requestName.equals(row.get("RequestName")))
                .collect(Collectors.toList());

        // Convert List<Map<String, String>> to Object[][]
        Object[][] data = new Object[filteredData.size()][1];
        for (int i = 0; i < filteredData.size(); i++) {
            data[i][0] = filteredData.get(i);
        }

        return data;
    }
}

