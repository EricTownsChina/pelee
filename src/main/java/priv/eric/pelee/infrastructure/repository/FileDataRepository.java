package priv.eric.pelee.infrastructure.repository;

import org.springframework.stereotype.Repository;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Description: 文件数据仓库，负责读取JSON文件并转换为Java对象
 *
 * @author EricTowns
 * @date 2026/1/20 20:55
 */
@Repository
public class FileDataRepository {

    private static final String JSON_FILE_EXTENSION = ".json";
    private static final String UNSUPPORTED_FILE_TYPE_ERROR = "只支持JSON类型文件加载，当前文件类型: ";

    /**
     * 从指定路径读取JSON文件并转换为Java对象
     *
     * @param path 文件路径
     * @param clazz 目标类型
     * @return 转换后的对象
     * @throws IOException 文件读取失败时抛出异常
     * @throws IllegalArgumentException 文件类型不支持时抛出异常
     */
    public <T> T loadJsonFile(Path path, Class<T> clazz) throws IOException {
        validateJsonFile(path);
        String data = Files.readString(path);
        return JsonUtil.fromJson(data, clazz);
    }

    /**
     * 从指定路径读取JSON文件并转换为Java对象
     *
     * @param path 文件路径字符串
     * @param clazz 目标类型
     * @return 转换后的对象
     * @throws IOException 文件读取失败时抛出异常
     * @throws IllegalArgumentException 文件类型不支持时抛出异常
     */
    public <T> T loadJsonFile(String path, Class<T> clazz) throws IOException {
        Path filePath = Path.of(path);
        return loadJsonFile(filePath, clazz);
    }

    /**
     * 验证文件是否为JSON文件
     * @param path 文件路径
     * @throws IllegalArgumentException 文件类型不支持时抛出异常
     */
    private void validateJsonFile(Path path) {
        String filename = path.getFileName().toString();
        if (!filename.endsWith(JSON_FILE_EXTENSION)) {
            throw new IllegalArgumentException(UNSUPPORTED_FILE_TYPE_ERROR + getFileExtension(filename));
        }
    }

    /**
     * 获取文件扩展名
     * @param filename 文件名
     * @return 文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }

}