package cn.wang.chatgpt.data.config;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.utils.InnerWordCharUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SensitiveWordConfig {

    @Bean
    public SensitiveWordBs sensitiveWordBs()
    {
        return SensitiveWordBs.newInstance()
                /**
                 * StringBuilder stringBuilder：这是一个 StringBuilder 对象，用于构建最终替换后的字符串。在这个方法中，敏感词替换后的结果会被追加到这个 StringBuilder 中。
                 *
                 * final char[] rawChars：这个参数表示原始的字符数组，即包含了待处理的文本内容。敏感词的位置可以通过 wordResult 参数提供的索引信息在这个字符数组中定位。
                 *
                 * IWordResult wordResult：这是一个接口类型的参数，用于提供敏感词的相关信息，如起始索引、结束索引等。通过这些信息，可以在 rawChars 中准确地定位敏感词。
                 *
                 * IWordContext wordContext：这个参数可能是用来提供上下文信息或者环境配置的接口，以便在替换敏感词时能够获取额外的信息或执行特定的操作。
                 */
                .wordReplace(((stringBuilder, chars, WordResult, WordContext) -> {
                    System.out.println(stringBuilder+" :" + chars +" : "+WordContext+": "+WordContext);
                    String sensitiveWord = InnerWordCharUtils.getString(chars,WordResult);
                    log.info("检测到敏感词：{}",sensitiveWord);

                    int wordLength = WordResult.endIndex() - WordResult.startIndex();
                    for (int i = 0; i < wordLength; i++) {
                        stringBuilder.append("*");
                    }


                })).ignoreCase(true)
                .ignoreWidth(true)
                .ignoreNumStyle(true)
                .ignoreChineseStyle(true)
                .ignoreEnglishStyle(true)
                .ignoreRepeat(false)
                .enableNumCheck(true)
                .enableEmailCheck(true)
                .enableUrlCheck(true)
                .enableWordCheck(true)
                .numCheckLen(1024)
                .init();
    }
}
