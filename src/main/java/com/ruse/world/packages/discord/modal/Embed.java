package com.ruse.world.packages.discord.modal;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.io.File;

public class Embed {

    private final String title, desc, author, footer,  thumbnail;
    private  String image;
    private final Color color;
    private final File images;
    private String[] field, inlines;
    public Embed(String title, String desc, String author, Color color, String footer, File image, String thumbnail, String[] field, String[] inlines){
        this.title = title;
        this.desc = desc;
        this.author = author;
        this.color = color;
        this.footer = footer;
        this.images = image;
        this.thumbnail = thumbnail;
        this.field = field;
        this.inlines = inlines;
    }

    public Embed(String title, String desc, String author, Color color, String footer, File image, String thumbnail, String[] field){
        this.title = title;
        this.desc = desc;
        this.author = author;
        this.color = color;
        this.footer = footer;
        this.images = image;
        this.thumbnail = thumbnail;
        this.field = field;
    }

    public Embed(String title, String desc, String author, Color color, String footer, File image, String thumbnail){
        this.title = title;
        this.desc = desc;
        this.author = author;
        this.color = color;
        this.footer = footer;
        this.images = image;
        this.thumbnail = thumbnail;
    }

    public EmbedBuilder getBuilder(){
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(title)
                .setDescription(desc)
                ;
        if(author != null)
            builder.setAuthor(author);
        if(color != null)
            builder.setColor(color);
        if(footer != null)
            builder.setFooter(footer);
        if(images != null)
            builder.setImage(images);
        if(thumbnail != null)
            builder.setThumbnail(thumbnail);
        if(field != null){
            for(String s : field){
                String[] split = s.split(":");
                builder.addField(split[0], split[1]);
            }
        }
        if(inlines != null){
            for(String s : inlines){
                String[] split = s.split(":");
                builder.addInlineField(split[0], split[1]);
            }
        }
        return builder;
    }
}
