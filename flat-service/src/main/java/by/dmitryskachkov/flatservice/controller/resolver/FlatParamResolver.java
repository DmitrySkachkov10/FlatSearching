package by.dmitryskachkov.flatservice.controller.resolver;

import by.dmitryskachkov.flatservice.core.dto.FlatFilter;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;

@Component
public class FlatParamResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(FlatFilter.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        FlatFilter flatFilter = new FlatFilter();

        flatFilter.setPage(getParameter(webRequest, "page", 1));
        flatFilter.setSize(getParameter(webRequest, "size", 20));
        flatFilter.setPriceFrom(getParameter(webRequest, "price[from]", 0));
        flatFilter.setPriceTo(getParameter(webRequest, "price[to]", Integer.MAX_VALUE));
        flatFilter.setBedroomsFrom(getParameter(webRequest, "bedrooms[from]", 0));
        flatFilter.setBedroomsTo(getParameter(webRequest, "bedrooms[to]", Integer.MAX_VALUE));
        flatFilter.setAreaFrom(getParameter(webRequest, "area[from]", 0.0f));
        flatFilter.setAreaTo(getParameter(webRequest, "area[to]", Float.MAX_VALUE));
        flatFilter.setFloors(getParameterList(webRequest, "floors"));
        flatFilter.setPhoto(getParameter(webRequest, "photo", false));

        return flatFilter;
    }

    private int getParameter(NativeWebRequest webRequest, String paramName, int defaultValue) {
        String paramValue = webRequest.getParameter(paramName);
        return paramValue != null ? Integer.parseInt(paramValue) : defaultValue;
    }

    private float getParameter(NativeWebRequest webRequest, String paramName, float defaultValue) {
        String paramValue = webRequest.getParameter(paramName);
        return paramValue != null ? Float.parseFloat(paramValue) : defaultValue;
    }

    private boolean getParameter(NativeWebRequest webRequest, String paramName, boolean defaultValue) {
        String paramValue = webRequest.getParameter(paramName);
        return paramValue != null ? Boolean.parseBoolean(paramValue) : defaultValue;
    }

    private List<Integer> getParameterList(NativeWebRequest webRequest, String paramName) {
        String[] paramValues = webRequest.getParameterValues(paramName);
        return paramValues != null ? Arrays.asList(paramValues).stream().map(Integer::parseInt).toList() : null;
    }
}
