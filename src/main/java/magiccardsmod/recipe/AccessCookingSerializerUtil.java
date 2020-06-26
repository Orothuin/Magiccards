package magiccardsmod.recipe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class AccessCookingSerializerUtil {

	@SuppressWarnings("rawtypes")
	public static CookingRecipeSerializer<?> getSerializer() {

		Class<?> ifactoryClass;

		ifactoryClass = CookingRecipeSerializer.class.getDeclaredClasses()[0];

		InvocationHandler handler = getHandler();

		Class[] interfaces = new Class[] { ifactoryClass };

		Object ifactoryProxyObject = Proxy.newProxyInstance(ifactoryClass.getClassLoader(), interfaces, handler);
		
		return getSerializerObject(ifactoryClass, ifactoryProxyObject);
	}

	private static InvocationHandler getHandler() {

		InvocationHandler handler = new InvocationHandler() {

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				if (args == null)
					return method.invoke(null, args);

				return new WetCardRecipe((ResourceLocation) args[0], (String) args[1], (Ingredient) args[2],
						(ItemStack) args[3], (float) args[4], (int) args[5]);
			}
		};
		return handler;
	}
	
	@SuppressWarnings("rawtypes")
	private static CookingRecipeSerializer<?> getSerializerObject(Class<?> ifactoryClass, Object ifactoryProxyObject){
		
		Class<CookingRecipeSerializer> serializer = CookingRecipeSerializer.class;

		Class partypes[] = new Class[] { ifactoryClass, Integer.TYPE };

		try {
			Constructor<CookingRecipeSerializer> ct = serializer.getConstructor(partypes);

			Object arglist[] = new Object[] { ifactoryProxyObject, 2 };

			Object retobj = ct.newInstance(arglist);

			return (CookingRecipeSerializer<?>) retobj;

		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
