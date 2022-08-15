package com.zazow.livingenchantments.attributes;

import com.zazow.livingenchantments.LEMod;
import com.zazow.livingenchantments.command.LECommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = LEMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributeRegistry {
    public static Supplier<IForgeRegistry<Personality>> personalityRegistrySupplier;
    public static Supplier<IForgeRegistry<Talent>> talentRegistrySupplier;

    public static Map<Personality.Rarity, List<ResourceLocation>> personalitiesByRarity = new HashMap<>();
    public static Map<Talent.Target, List<ResourceLocation>> talentByTarget = new HashMap<>();

    @SubscribeEvent
    public  static void newRegistry(NewRegistryEvent event) {
        RegistryBuilder<Personality> personalityBuilder = new RegistryBuilder<>();
        personalityRegistrySupplier = event.create(personalityBuilder
                .setName(new ResourceLocation(LEMod.MODID, "personality_registry"))
                .setType(Personality.class)
        );

        RegistryBuilder<Talent> talentBuilder = new RegistryBuilder<>();
        talentRegistrySupplier = event.create(talentBuilder
                .setName(new ResourceLocation(LEMod.MODID, "talent_registry"))
                .setType(Talent.class)
        );
    }
    @SubscribeEvent
    public static void registerPersonalities(RegistryEvent.Register<Personality> event) {
        Personality[] personalities = {
                new Personality("Clumsy", 0.5f, 0,0.6, 0.05f,
                        new String[]{"Ooops, my bad...", "Ahhh I slipped", "Man it's not my day."},
                        new String[]{"I slipped right on its groin."},
                        new String[]{"Guess I'm not the only clumsy one."},
                        new String[]{"Man I'm feeling lucky", "Wow I feel like this clumsiness thing is behind me."},
                        new String[]{},
                        new String[]{"Watch out man, I'm $durability from breaking!"},
                        new String[]{"Duuude fix meeee, my durability is $durability"},
                        new String[]{"Goodbye old friend :'("}, Personality.Rarity.COMMON),
                new Personality("Nerdy", 0.3f, 0.1f, 1.5f, 0.15f,
                        new String[]{"Hueueue did you know that unbreaking applies a chance for your tool to not take damage. It doesn't increase its durability huehueuee.",
                                "Hueueueueueue that popped my zit", "Do you think I have a chance with that librarian huehuee"},
                        new String[]{"The power of knowledge never fails to kill", "I bored him to death huehueeee"},
                        new String[]{"Huehueueee here's a book about getting better."},
                        new String[]{"I finished another book! HUeuhueueuheuee", "I can read much faster now, thatill impress the ladies huehuehue"},
                        new String[]{"Ouch mommy that hurtss"},
                        new String[]{"I can't read as fast, I'm $durability from breaking!"},
                        new String[]{"I need to find a book about mending, my durability is $durability"},
                        new String[]{"Huehue, I guess it was a good run"}, Personality.Rarity.COMMON),
                new Personality("Lazy", 1, 0.4f, 0.3, 0.03f,
                        new String[]{"That's it. We're done... right?", "Hey, $user, we're done now, right?",
                                "Please no more...", "$user, please, I can't take it anymore!",
                                "I don't know how much more work I can take!", "$user, I'm not sure I'll last much longer!",
                                "All this work is killing me..", "Wow, $user, this is exhausting work!",
                                "Do you really need to do this? Wouldn't you rather go sleep? I would...",
                                "C'mon, lets go sleep now. I'm tired of working...", "$user, enough work. Lets go take a nap!",
                                "No more! Please! I'm begging you, $user", "Are we done yet?"},
                        new String[]{"Look, $user, they're dead.", "Nice one, $user!", "Killing is so much work! I'm so tired!",
                                "Do we have to kill them, $user? Can't we just sleep?", "I just want a break, $user"},
                        new String[]{"Finally, I can rest too!", "Ouch! You look like you're in pain, $user.",
                                "Make sure to come back for me, $user!", "Don't forget about me, $user!"},
                        new String[]{"Ah yes, more power! Now back to sleep... (Level $level)",
                                "I can feel the power flowing through me, $user! ... and now I'm bored. (Level $level)",
                                "I feel... strong, $user (Level $level)!", "Is this what it feels like to be good for something? (Level $level)"},
                        new String[]{"Ouch! I bet they'll feel that one tomorrow, $user!", "Ooo, that looks like it had to hurt!",
                                "Ouch!", "All this combat is making me so tired, $user!", "Take that!"},
                        new String[]{"I'm growing weaker, $user. ($durability durability remaining)",
                                "I'm starting to feel weak, $user. ($durability durability remaining)",
                                "Don't forget about me, $user! ($durability durability remaining)"},
                        new String[]{"Is the end finally here? ($durability durability remaining!)",
                                "Is this where we part ways, $user? ($durability durability remaining!)",
                                "I'm not ready to die yet, $user! ($durability durability remaining!)"},
                        new String[]{"Finally, the end is here"}, Personality.Rarity.COMMON),
                new Personality("Lewd", 0.8f, 0, 0.7f, 0.15f,
                        new String[]{"Yes... yes.. yes!!!", "Use me more, $user! Yes!", "I love it when you use me like that, $user.",
                                "Oh $user, you know just what I want!", "Yes, $user! Yes! Use me JUST like that!",
                                "Oh god it feels so good, $user!", "More! more! MORE!", "God this just feels SO good!",
                                "$user, I hope this never stops!", "$user, I feel so good!", "Keep going, $user!",
                                "Oh yes, just like that $user!", "Quick $user, quick! More!", "Yeeeees! It's so GOOD!",
                                "Oh my gosh, $user, it just feels so good!", "I'll never be able to get enough of this!",
                                "I need more, $user!", "More!", "Yes!", "Give it to me, $user!"},
                        new String[]{"They died!"},
                        new String[]{"Aww, did you die, $user? Hurry back!", "Don't die on me, $user! I need you!",
                                "Hurry back! I need you, $user!", "Don't leave me here, $user! I need to be used!"},
                        new String[]{ "Oh it feels so good, $user! (Level $level)", "My power is growing, $user! (Level $level)",
                                "I feel so reinvigorated! (Level $level)", "So this what living feels like! (Level $level)",
                                "This power... it feels so good! (Level $level)", "I could get used to this... (Level $level)"},
                        new String[]{"I bet that one hurt.", "It's not supposed to feel good.", "I wish $user would slap me around like this!",
                                "Just a little more, $user."},
                        new String[]{"I'm growing weaker, $user. ($durability durability remaining)",
                                "I'm starting to feel weak, $user. ($durability durability remaining)",
                                "Don't forget about me, $user! ($durability durability remaining)"},
                        new String[]{"I don't feel good at all... ($durability durability remaining!)",
                                "This can't be how this ends, $user! ($durability durability remaining!)",
                                "I'm not ready to die yet, $user! ($durability durability remaining!)"},
                        new String[]{"I enjoyed you holding me while it lasted :*"}, Personality.Rarity.UNCOMMON),
                new Personality("Depressed", 0.9f, 0.4f, 0.5f, 0.05f,
                        new String[]{"$user, can this wait? I'm really tired.", "Do you really need to use me now, $user?",
                                "...Sigh...", "I just wanna sleep...", "Are we almost done yet, $user? I'm really tired.",
                                "Don't be gentle, I'm already broken", "Can I break already?"},
                        new String[]{"They're better off now anyway, $user",
                                "It's unfortunate I can't get you to kill me instead, $user", "...Lucky", "If only..."},
                        new String[]{"No $user, Not you too!", "Don't leave me here, $user! Take me with you!",
                                "You're lucky, $user...", "$user, I wish I was you... If I only was so fortunate"},
                        new String[]{"$user, I feel a bit better ($level)",
                                "Hey, $user, I'm starting to feel better... nope, wait, I'm not. ($level)",
                                "I don't deserve this, $user ($level)", "Why help me, $user? ($level)",
                                "$user, just stop. I don't need your sympathy ($level)."},
                        new String[]{"That's not very nice of you, $user...", "Maybe you shouldn't hurt others, $user...",
                                "$user, is that really nice of you?", "$user, when will you stop swinging me around? I'm tired."},
                        new String[]{"My weakness is growing, $user. ($durability durability remaining)",
                                "I'm starting to feel weaker, $user. ($durability durability remaining)",
                                "Don't forget about me, $user! ($durability durability remaining)"},
                        new String[]{"Is the end finally here? ($durability durability remaining!)",
                                "Is this where we part ways, $user? ($durability durability remaining!)",
                                "I'm ready, $user... ($durability durability remaining!)"},
                        new String[]{"Man, I though breaking would feel better than this."}, Personality.Rarity.UNCOMMON),
                new Personality("Energetic", 0.8f, 0, 2,0.5f,
                        new String[]{"Again! Again!", "This is so fun, let's keep doing it, $user!", "Keep going!", "Yay!",
                                "I could do this all day!", "$user, don't stop! This is so much fun!", "I want more!",
                                "This is exciting! Don't you agree, $user?"},
                        new String[]{"We really showed them!", "Good job, $user!", "Killing is so much work! I love it!"},
                        new String[]{"Oh no! Is this the end?", "Noooo! We were only getting started!",
                                "We'll get them next time, right? $user? ...oh, you died.", "Don't forget about me, $user!"},
                        new String[]{"Ah yes, more power! (Level $level)",
                                "I can feel the power flowing through me, $user! Let's keep going! (Level $level)",
                                "I feel stronger already, $user! (Level $level)!"},
                        new String[]{ "Ouch! I bet they'll feel that one tomorrow, $user!", "Ooo, that looks like it had to hurt!",
                                "Ouch!", "Take that!"},
                        new String[]{"I'm starting to hurt, $user. ($durability durability remaining)",
                                "I'm feeling weaker, $user. ($durability durability remaining)",
                                "Don't forget about me, $user! ($durability durability remaining)"},
                        new String[]{"(oof...) Is... is this the end of our adventure, $user? ($durability durability remaining!)",
                                "I don't feel so good, $user... ($durability durability remaining!)",
                                "I don't wanna die, $user! ($durability durability remaining!)",
                                "I guess... this is it, huh $user? ($durability durability remaining!)"},
                        new String[]{"I'm coming back for you, $user!"}, Personality.Rarity.RARE),
                new Personality("Silent", 1, 0.5f, 0.8, 0.4f,
                        new String[]{"..."},
                        new String[]{"..."},
                        new String[]{"..."},
                        new String[]{"... (Level $level)"},
                        new String[]{"..."},
                        new String[]{"... ($durability durability remaining)"},
                        new String[]{"... ($durability durability remaining)"},
                        new String[]{"bye"}, Personality.Rarity.RARE),
                new Personality("Cat", 0.8f, 0.5f, 0.8, 0.08f,
                        new String[]{"Meow.", "Meeoow.", "Meew.", "Hiss!", "Meow?"},
                        new String[]{"Meow!", "Hiss!", "Hisss!"},
                        new String[]{"Meow...", "Meow... meow?", "Meow...?"},
                        new String[]{"Meow! (Level $level)"},
                        new String[]{ "Meow!", "Hiss!", "Hiss!", "Hisss!"},
                        new String[]{"Meow! ($durability durability remaining)", "Meow? ($durability durability remaining)"},
                        new String[]{"Growl! ($durability durability remaining)", "Meow! ($durability durability remaining)",
                                "Meow??? Bark Bark! ($durability durability remaining)"},
                        new String[]{"Mewwwwwwwwww </3"}, Personality.Rarity.EPIC),
                new Personality("Dog", 0.9f, 0.3f, 1, 0.15f,
                        new String[]{"Woof.", "Howl.", "Whimper.", "Bark! Bark! Bark!", "*Sniffs*"},
                        new String[]{"Bark!", "Bark bark bark!", "Bark! Bark! Bark!"},
                        new String[]{"Whimper.", "Whine."},
                        new String[]{"Bark! (Level $level)", "Woof! (Level $level)"},
                        new String[]{"Woof!", "Bark!", "Bark bark bark!", "Howl.", "Low growl."},
                        new String[]{"Wimpers. ($durability durability remaining)", "Whine. ($durability durability remaining)",
                                "Bark! Bark Bark! ($durability durability remaining)"},
                        new String[]{"Wimpers. ($durability durability remaining)", "Whine. ($durability durability remaining)",
                                "Bark! Bark Bark! ($durability durability remaining)"},
                        new String[]{"woooof </3"}, Personality.Rarity.EPIC),
                new Personality("Glitchy", 0.6f, 0.2f, 1.5f,0.2f,
                        new String[]{"§k c  drtbs fn  Wsneathc gt n  e. ceiapk½p  s t+thicFe  dce", "§kemono,h    ?. eesf echh²aeane=e.TetVtd e²e½t.",
                                "§k slw  ½i  e b  wfd ovty t sa½wtuhHirchrt iecu)aoirc", "§kcaygVennenr slw  ½i H",
                                "§k b  wfd ovty t sa½wtuhHirchrt iecu)aoir"},
                        new String[]{"§k caf  lwu s  derh=ccn wae epo a    a   x", "§kyV upneiehkah", "§kl(  un tnyW  arta( "},
                        new String[]{"§kshs( i t. h oune pyf atw  oosWha rts tbmedhhe( r  = fei)lWifpndi s a tr b(ay t u cb  caf  lwu s",
                                "§kneiehkah tfc eept ½mo  ete +fla)tl a ky e  neep rlc lw u nx knmcT es nqimq .i ol ot c c/qon ",
                                "§ktalhl(  un tnyW"},
                        new String[]{"§kpe ecofs soyasd§r (Level $level)", "§kt morsa ) kam detiriti §r (Level $level)",
                                "§kso+ Wt)ci  ten idqr k om n²tia rron = i o§r (Level $level)"},
                        new String[]{"§kKary  d", "§kape /nhros,n h", "§kk pyn cr i  i  tkho ic d(m  t)t", "§krsdile"},
                        new String[]{"§km a displacement kinetic energy of the resultant x? We caus§r ($durability durability remaining)",
                                "§kin call product of a particle o§r ($durability durability remaining)",
                                "§k then stant and V - ½ ( ½ ( V is the represe§r ($durability durability remaining)"},
                        new String[]{"§kd the squationstant x? We mas§r ($durability durability remaining!)",
                                "§krticle of the resent acti§r ($durability durability remaining!)",
                                "§k stant and V - ½ ( ½ ( V is the represent kineticle on a partic energy of its speed at t§r ($durability durability remaining!)",},
                        new String[]{"§ktalhl(  un tnyW"}, Personality.Rarity.LEGENDARY),
                new Personality("Alien", 1, 0.2f, 1, 0.04f,
                        new String[]{"⌇⍾⏃⟒⏃⟒ ⏃⍜⋉⟒", "⟟⋉ ⊬⟟⋉ ⊬⟟⌖⟊⏃☍ ⍾⏃⟒ ⌖⏃ ⌇⍜⋏☌?", "☌⏃⟒ ⍀⏃⟒⍾ ⊬⍜⍀⏁ ⋉⏃", "⍜⋏⏃ ⟒⌇⏃⟒⎍ ⏁⟟⍀⟒⎅ ⟒⌇⏃⍾, ⌇⎍⋉⍜⍀?"},
                        new String[]{"⊬⏃ ⋔☊⊑⌰'⏁ ⟟⋉ ⟟⟒☌⎅", "⊬⟟⋉ ⏃⟒⍀⏃ ⟟⋉ ⋉⟟⍀⏃", "⊬⏃⟒⌇ ⌰⏃⏚⏚⟟⏃ ⍜ ⋔⏃⟒⏃⟒⊑",
                                "⊬⏃⟒⌇ ☍⏃⟒⍾ ⌇'⍜⍾ ⊬⏃⟒⌇ ⎅⍀⏃⌰⌇⟊⟟☍", "⎍⌇⟒⍀, ⊬⍜⍾ ⟟⋉ ⋔⏃⟒⋏ ⟒⌇⏃⟒⎍"},
                        new String[]{"⋔⏃⟒⏃⟒⊑, ⌇⏃⟒⌰ ☌⍜⋏⏃ ⟒⌇⏃⟒⎍ ☌⟟⏃ ⏃⟒⍀ ⋉⏃!", "⟟ ⍜⊑⌰⍜⟒⌇⋉ ⏁⍀⏃⌰ ⟒⌇⏃⟒⎍ ⌰⏃⏚⏚⟟⏃ ⌰⏃⍜⏁", "⎅⍜⊬⏃⍾⟟⊬",
                                "⎍⌇⟒⍀, ☌⏃⟒ ⍀⏃⟒⍾ ☌⟟⏃ ⏃⟒⍀ ⋉⏃!", "⎍⌇⟒⍀, ⍀⏃⟒!"},
                        new String[]{"⋉⟒⌇ ⎅⏃⟒⌰⏃⏚⏚⟟ ☌⍀⍜⍙⌇ ⍾⏃⟒ ☊⊑⍙⏃⊑! (Level $level)", "⋉⏃ ⋔⏃⏃⊑ ⋉⏃⟒ ⟒⎅⋏⏃⟒⍀⎅⊑, ⍀⏃⟒⌰ ⊬⍜⍾ ⋉⏃ ☊⊑⍙⏃⊑! (Level $level)",
                                "⟟ ⋉⏃⏃ ⊬⏃ ⌰⏃⟒⋏⊑☌. (Level $level)", "⎍⌇⟒⍀, ⌖⏃ ⊬⍜⋏⏃⋔⎍⊑, ⟟ ⍜⋉ ⍀⏃⟒⌰ ☊⊑⍙⏃⍙! (Level $level)"},
                        new String[]{"⍾⍜⟒⎅⏃ ⋉⟒⌇ ⍙⟒⎅⍀☍", "⟒⌇⏃⟒⎍ ⌰'⏁⍜⋉⋉ ☍⏃⋉☍!", "⟒⌇⏃⟒⎍ ⌰⟟⋉⋉ ⋔⍜⟟⊑", "⍀⟒ ⋉⟒⟒⏚⋏⟟⍀ ⟒⋉ ⍜⟒⌿.",
                                "⍾⍜⟒⎅⏃ ⍾⋏⎍⏃ ⎅⍜⟟⍀!", "⟒⌇⏃⟒⎍ ⋔⏃⟒⏃⟒⊑⟟⌰'⏁ ⊬⋏⏃⍜⍾⎍⋏⏃!", "☊⊑⍾ ⎍⋉ ⎐⏃⋏⍾⎍⟟⌇⊑ ⊬⟟⋉ ⎎⍜⟒, ⎍⌇⟒⍀!",
                                "⎍⌇⟒⍀, ⊬⟟⋉ ⊬⋏⏃⍜⍾⎍⋏⏃ ☌⏃⟒⏃⋉ ⍀⏃⟒⍾ ⏁⍀⏃⟒⌰ ⏃⟒⎍⋏ ⟒⎅⋏⏃⍀⎅⊑⊬! ☊⊑⍾ ⎍⋉ ⌰'⏁⏃⟒⌰ ⟟⍾!",
                                "⟒⌇⏃⟒⎍ ⌰⟟⋉⋉ ⏁⍀⏃⟒⌰ ⊑⏃⟒⋉⏃", "⎍⌇⟒⍀, ⊬⟟⋉ ⊬⋏⏃⍜⍾⎍⋏⏃ ⋉⍜⟒⌇ ⍀⏃⟒⍾ ⌖⏃ ⏃⌰⌰⍜⍙⟒⎅ ⍾⏃⟒ ⏃⌿⟟⟒⎅!"},
                        new String[]{"⋔⊬ ⌰⟟⎎⟒ ⟒⌇⌇⟒⋏☊⟒ ⟟⌇ ☌⟒⏁⏁⟟⋏☌ ⎎⏃⟟⍀⌰⊬ ⌰⍜⍙... ($durability durability remaining)",
                                "⟟⌇⋏'⏁ ⟟⏁ ⏁⟟⋔⟒ ⊬⍜⎍ ⍀⟒⌿⏃⟟⍀⟒⎅ ⋔⟒? ($durability durability remaining)", "⟟'⋔ ⎎⟒⟒⌰⟟⋏☌ ⌿⍀⟒⏁⏁⊬ ⍙⟒⏃☍ ($durability durability remaining)"},
                        new String[]{"⋔⊬ ⌰⟟⎎⟒ ⟒⌇⌇⟒⋏☊⟒ ⟟⌇ ☌⟒⏁⏁⟟⋏☌ ⎎⏃⟟⍀⌰⊬ ⌰⍜⍙... ($durability durability remaining!)",
                                "⟟⌇⋏'⏁ ⟟⏁ ⏁⟟⋔⟒ ⊬⍜⎍ ⍀⟒⌿⏃⟟⍀⟒⎅ ⋔⟒? ($durability durability remaining!)", "⟟'⋔ ⎎⟒⟒⌰⟟⋏☌ ⌿⍀⟒⏁⏁⊬ ⍙⟒⏃☍ ($durability durability remaining!)"},
                        new String[]{"⟟⋔⟒ ⊬⍜⎍ ⍀⟒⌿⏃⟟⍀⟒~"}, Personality.Rarity.LEGENDARY),
                new Personality("Demonic", 1.5f, 0.3f, 0.5f,0.3f,
                        new String[]{"sqaeae aoze", "iz yiz yixjak qae xa song?", "gae raeq yort za", "ona esaeu tired esaq, suzor?"},
                        new String[]{"ya mchl't iz iegd", "yiz aera iz zira", "yaes labbia o maeaeh", "yaes kaeq s'oq yaes dralsjik",
                                "$user, yoq iz maen esaeu"},
                        new String[]{"Maeaeh, sael gona esaeu gia aer za!", "I ohloesz tral esaeu labbia laot", "Doyaqiy",
                                "$user, gae raeq gia aer za!", "$user, rae!"},
                        new String[]{"Zes daelabbi grows qae chwah! (Level $level)", "za maah zae ednaerdh, rael yoq za chwah! (Level $level)",
                                "I zaa ya laenhg. (Level $level)", "$user, xa yonamuh, I oz rael chwaw! (Level $level)"},
                        new String[]{"Qoeda zes wedrk", "Esaeu l'tozz kazk!", "esaeu lizz moih", "re zeebnir ez oep.",
                                "qoeda qnua doir!", "esaeu maeaehil't ynaoquna!", "chq uz vanquish yiz foe, $user!",
                                "$user, yiz ynaoquna gaeaz raeq trael aeun ednardhy! chq uz l'tael iq!", "Esaeu lizz trael haeza",
                                "$user, yiz ynaoquna zoes raeq xa allowed qae apied!"},
                        new String[]{"$user, ao bnzl zegsl ni jlmgbo dlwblhld! ($durability durability remaining)",
                                "Oep apih jeh zegvlh he rlmb al, $user! ($durability durability remaining)",
                                "Ao lmcjlii vgei... ($durability durability remaining)"},
                        new String[]{"$user, ao bnzl zegsl ni jlmgbo dlwblhld! ($durability durability remaining)",
                                "Oep apih jeh zegvlh he rlmb al, $user! ($durability durability remaining)",
                                "Ao lmcjlii vgei... ($durability durability remaining)"},
                        new String[]{"sasdfel gondda essgaeu gia aer za!"}, Personality.Rarity.LEGENDARY)
        };

        for (Personality personality : personalities) {
            event.getRegistry().register(personality);

            if (!personalitiesByRarity.containsKey(personality.rarity)) {
                personalitiesByRarity.put(personality.rarity, new ArrayList<>());
            }

            personalitiesByRarity.get(personality.rarity).add(personality.getRegistryName());
        }

        event.getRegistry().registerAll();
        LECommand.init();
    }

    public static final Talent VEIN_MINER_TALENT = new VeinTalent("vein_miner", false);
    public static final Talent GRID_MINER_TALENT = new VeinTalent("grid_miner", true);
    public static final Talent LIFE_LEECH_TALENT = new LifeLeechTalent("life_leech");
    public static final Talent FEED_TALENT = new FeedTalent("feed");
    public static final Talent THORNS_TALENT = new ThornsTalent("thorns");
    public static final Talent TEMPO_TALENT = new TempoTalent("tempo");
    public static final Talent PROSPERITY_TALENT = new ProsperityTalent("prosperity");
    public static final Talent FLAME_BENDER_TALENT = new FlameBenderTalent("fire_resistance");
    public static final Talent NINJA_TALENT = new Talent("ninja", true, Talent.Target.HELMET, Talent.Target.LEGGINGS, Talent.Target.BOOTS); // NOT IMPLEMENTED
    public static final Talent SUMMONING_TALENT = new Talent("summoning", true, Talent.Target.WEAPON); // NOT IMPLEMENTED
    public static final Talent MEDITATION_TALENT = new MeditationTalent("meditation");
    public static final Talent ENDER_PR_TALENT = new Talent("ender_pr", true, Talent.Target.HELMET);
    public static final Talent SAVIOR_TALENT = new SaviorTalent("savior");
    public static final Talent PROTECTOR_TALENT = new Talent("protector", true, Talent.Target.HELMET,  Talent.Target.CHESTPLATE,  Talent.Target.LEGGINGS, Talent.Target.BOOTS);
    public static final Talent VITALITY_TALENT = new VitalityTalent("vitality");

    @SubscribeEvent
    public static void registerTalents(RegistryEvent.Register<Talent> event) {
        Talent[] talents = new Talent[]{
                VEIN_MINER_TALENT,
                GRID_MINER_TALENT,
                LIFE_LEECH_TALENT,
                FEED_TALENT,
                THORNS_TALENT,
                TEMPO_TALENT,
                PROSPERITY_TALENT,
                FLAME_BENDER_TALENT,
                NINJA_TALENT,
                SUMMONING_TALENT,
                MEDITATION_TALENT,
                ENDER_PR_TALENT,
                SAVIOR_TALENT,
                PROTECTOR_TALENT,
                VITALITY_TALENT
        };

        for (Talent talent : talents) {
            event.getRegistry().register(talent);
            for (Talent.Target target : talent.targets) {
                if (!talentByTarget.containsKey(target)) {
                    talentByTarget.put(target, new ArrayList<>());
                }

                talentByTarget.get(target).add(talent.getRegistryName());
            }
        }

        event.getRegistry().registerAll();
        LECommand.init();
    }
}
