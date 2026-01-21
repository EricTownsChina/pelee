package priv.eric.pelee.infrastructure.repository;

import org.springframework.stereotype.Repository;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Description: 读取路径数据并加载为Java对象或Properties的仓库类
 *
 * @author EricTowns
 * @date 2026/1/20 20:55
 */
@Repository
public class FileDataRepository {

    private static final String JSON_FILE_EXTENSION = ".json";

    /**
     * 从指定路径读取json文件并转换为Java对象
     *
     * @param path 路径
     * @return 映射对象
     * @throws IOException 读取失败时抛出异常
     */
    public <T> T loadJsonFile(Path path, Class<T> clazz) throws IOException {
        final String filename = path.getFileName().toString();
        if (filename.endsWith(JSON_FILE_EXTENSION)) {
            String data = Files.readString(path);
            return JsonUtil.fromJson(data, clazz);
        } else {
            throw new IllegalArgumentException("只支持json类型文件加载");
        }
    }

    /**
     * 从指定路径读取json文件并转换为Java对象
     *
     * @param path 路径
     * @return 映射对象
     * @throws IOException 读取失败时抛出异常
     */
    public <T> T loadJsonFile(String path, Class<T> clazz) throws IOException {
        if (null != path && path.endsWith(JSON_FILE_EXTENSION)) {
            Path filePath = Path.of(path);
            String data = Files.readString(filePath);
            return JsonUtil.fromJson(data, clazz);
        } else {
            throw new IllegalArgumentException("只支持json类型文件加载");
        }
    }

}