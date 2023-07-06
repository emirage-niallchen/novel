package cn.com.emirage.novel.core.auth;
import lombok.experimental.UtilityClass;


/**
 * @author ChenBoyun
 * @date 2023-07-04 19:45
 */
@UtilityClass
public class UserHolder {
    private static final ThreadLocal<Long> userIdTL = new ThreadLocal<>();
    private static final ThreadLocal<Long> authorIdTL= new ThreadLocal<>();
    public void setUserIdTL(Long userId) {
        userIdTL.set(userId);
    }
    public Long getUserId(){
        return userIdTL.get();
    }
    public void setAuthorIdTL(Long authorId) {
        authorIdTL.set(authorId);
    }
    public Long getAuthorId(){
        return authorIdTL.get();
    }
    public void clear(){
        userIdTL.remove();
        authorIdTL.remove();
    }
}
