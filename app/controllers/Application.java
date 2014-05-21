package controllers;

import java.util.HashMap;
import java.util.Map;

import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import play.mvc.WebSocket.Out;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Map<Integer, Out<String>> member = new HashMap<Integer, Out<String>>();
    
    public static WebSocket<String> ws(final int id) {
    	
    	return new WebSocket<String>() {
			@Override
			public void onReady(play.mvc.WebSocket.In<String> in,
					play.mvc.WebSocket.Out<String> out) {

				// チャットグループに保存する
				// このチャットグループは後々考えて保存、保持する必要がある
				// 例えばユーザーがログインした瞬間に関係のあるグループにputする必要がある
				// ここの例ではグループをひとつに限定し、自分が発言した時
				// 自分以外にブロードキャストする、というふうにする
				
		    	member.put(Integer.valueOf(id), out);
				
				// クライアントからメッセージを受信した時
				in.onMessage(new Callback<String>(){
					@Override
					public void invoke(String message) throws Throwable {
						
						// クライアントが発言した
						// 自分以外に送信する
						for (Map.Entry<Integer, Out<String>> e : member.entrySet()) {
							if (Integer.valueOf(id) != e.getKey()) {
								e.getValue().write(message);
							}
						}
						
					}
				});
				// クライアントから切断された時
				in.onClose(new Callback0(){
					@Override
					public void invoke() throws Throwable {
						Integer key = Integer.valueOf(id);
						if (member.containsKey(key)) {
							member.remove(key);
						}
					}
				});
			}
			
			
    		
    	};
    }

}
