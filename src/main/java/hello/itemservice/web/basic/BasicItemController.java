package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model
    ){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item); // 모델에 저장한 아이템 넣어서 상세 화면으로 쏴주고 싶다.
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item){
        // @ModelAttribute("param") -> 해당 param을 key로 가지는 모델을 생성해서 addAttribute로 넣어준다.
        // -> 즉 이름을 넣어주면 모델에 값이 자동으로 주입이 된다. -> addAttribute 안해줘도 됨.(생략 가능) -> 파라미터 model 안받아도 됨
        itemRepository.save(item);

        //model.addAttribute("item", item); // 모델에 저장한 아이템 넣어서 상세 화면으로 쏴주고 싶다.
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){
        // 이름 생략시 파라미터로 받는 클래스의 이름을 소문자로 변환해서 이름으로 만들고 모델에 넣어준다.
        // 즉 클래스명 Item -> item으로 바꾸고 이를 이름으로하는 모델 생성해서 addAttribute 실행해준다.
        itemRepository.save(item);

        //model.addAttribute("item", item); // 모델에 저장한 아이템 넣어서 상세 화면으로 쏴주고 싶다.
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV4(Item item){
        // Model Attribute 도 생략이 가능하다 (객체가 매개변수인 경우) -> 생략을 제외한 내용은 앞과 같다.
        itemRepository.save(item);

        //model.addAttribute("item", item); // 모델에 저장한 아이템 넣어서 상세 화면으로 쏴주고 싶다.
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV5(Item item){
        // Model Attribute 도 생략이 가능하다 (객체가 매개변수인 경우) -> 생략을 제외한 내용은 앞과 같다.
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
