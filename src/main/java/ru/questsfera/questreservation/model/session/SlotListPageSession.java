package ru.questsfera.questreservation.model.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.questsfera.questreservation.model.dto.SlotListPage;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SlotListPageSession {

    @Getter
    @Setter
    private SlotListPage slotListPage;
}
