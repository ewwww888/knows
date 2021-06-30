package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.model.Tag;
import cn.tedu.knows.portal.mapper.TagMapper;
import cn.tedu.knows.portal.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2021-06-25
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    //声明一个成员变量充当缓存
    //CopyOnWriteArrayList是一个线程安全的list集合
    private  List<Tag> tags = new CopyOnWriteArrayList<>();

    private Map<String,Tag> tagMap = new ConcurrentHashMap<>();


   /*法一:
    @Autowired
    private TagMapper tagMapper;
    @Override
    public List<Tag> getTags() {
        List<Tag> tags = tagMapper.selectList(null);
        return tags;
    }
    */

    /**
     * ServiceImpl类中
     * this.list()就是调用父类中已经提供的全查所有当前tag对象方法
     * list()连库
     */
    @Override
    public List<Tag> getTags() {
        if (tags.isEmpty()) {
            //2
            synchronized (tags) {
                if (tags.isEmpty()){
                    //1
                    tags.addAll(list());
                    for (Tag t : tags){
                        //以标签名称为key 标签对象为value 保存在tagmap
                        tagMap.put(t.getName(),t);
                    }
                }
            }
        }
        //List<Tag> tags = this.list();
        //list()方法是ServiceImpl类的
        return tags;
    }

    /**
     *
     *
     * @return
     */
    @Override
    public Map<String, Tag> getTagMap() {
        if (tagMap.isEmpty()){
            getTags();
        }
        return tagMap;
    }

}
