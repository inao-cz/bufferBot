package me.inao.discordbot.event;

import com.google.gson.JsonParser;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import me.inao.discordbot.Main;
import me.inao.discordbot.exception.NoSuchServerTextChannelException;
import me.inao.discordbot.ifaces.IListener;
import me.inao.discordbot.request.http.Driver;
import me.inao.discordbot.request.http.post.CaptchaCheckPostRequest;
import me.inao.discordbot.request.http.post.CaptchaDeletePostRequest;
import me.inao.discordbot.util.MessageSender;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.jsoup.Jsoup;

import java.awt.*;

@RequiredArgsConstructor
public class ReactionEvent implements ReactionAddListener, IListener {
    private final Main main;

    @Override
    public void onReactionAdd(ReactionAddEvent e) {
        if(e.getUser().isEmpty() || e.getServer().isEmpty() || e.getChannel().asServerTextChannel().isEmpty()) return;

        if (!e.getUser().get().isBot() && main.getConfig().isFeatureEnabled("captchaSystem") && e.getChannel().asServerTextChannel().get().getName().contains("captcha") && e.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":white_check_mark:"))) {
            CaptchaCheckPostRequest captcha = new CaptchaCheckPostRequest();
            if (main.getConfig().getFeatureValue("captchaSystem", "httpAuth") != null) {
                captcha.getArguments().put("auth", main.getConfig().getFeatureValue("captchaSystem", "httpAuth"));
            }
            captcha.getArguments().put("discordId", new String[]{e.getUserIdAsString()});
            String response = Jsoup.parse(new Driver(captcha).postRequestWithResponse()).text();
            boolean done = new JsonParser().parse(response).getAsJsonObject().get(e.getUserIdAsString()).getAsBoolean();
            if(done){
                e.getUser().get().removeRole(e.getServer().get().getRolesByName(main.getConfig().getFeatureData("captchaSystem").split(";")[1]).get(0));
                e.getUser().get().addRole(e.getServer().get().getRolesByName(main.getConfig().getFeatureData("captchaSystem").split(";")[2]).get(0));
                e.getChannel().asServerTextChannel().get().delete();
                CaptchaDeletePostRequest captchaDel = new CaptchaDeletePostRequest();
                if (main.getConfig().getFeatureValue("captchaSystem", "httpAuth") != null) {
                    captchaDel.getArguments().put("auth", main.getConfig().getFeatureValue("captchaSystem", "httpAuth"));
                }
                captchaDel.getArguments().put("discordId", new String[]{e.getUserIdAsString()});
                boolean sent = new Driver(captchaDel).postRequestWithoutResponse();
                if (main.getConfig().isFeatureEnabled("joinMessage") && sent) {
                    new MessageSender(e.getUser().get().getDiscriminatedName() + " has joined", main.getConfig().getMessage("welcome", "success").replace("%_user_%", e.getUser().get().getDiscriminatedName()), Color.GREEN, e.getServer().get().getChannelsByName(main.getConfig().getFeatureChannel("joinMessage")).get(0).asServerTextChannel().orElseThrow(NoSuchServerTextChannelException::new));
                }
            }
        }
    }
}
