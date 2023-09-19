package org.papiricoh.townyreligion.object.book;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class SacredBook {
    private String title;
    private String content;
    private String god_name;

    public SacredBook(String title, String content, String god_name) {
        this.title = title;
        this.content = content;
        this.god_name = god_name;
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getGod_name() {
        return god_name;
    }

    public void setGod_name(String god_name) {
        this.god_name = god_name;
    }

    public ItemStack getBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();

        bookMeta.setTitle(this.title);
        bookMeta.setAuthor("Holy Council");
        List<String> content = BookUtils.splitContentIntoPages(BookUtils.replaceGodNameInContent(this.content, this.god_name));
        for (String page : content) {
            bookMeta.addPage(page);
        }

        book.setItemMeta(bookMeta);
        return book;
    }
}
