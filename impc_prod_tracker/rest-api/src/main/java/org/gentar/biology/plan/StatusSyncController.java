package org.gentar.biology.plan;

import lombok.Data;
import org.gentar.biology.plan.filter.PlanFilterBuilder;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.helpers.CsvRecord;
import org.gentar.helpers.CsvWriter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/plans/statusSync")
@CrossOrigin(origins="*")
public class StatusSyncController
{
    private final PlanRepository planRepository;
    private final PlanService planService;
    private final CsvWriter<MassivePlanStatusUpdateCsv> csvWriter;

    public StatusSyncController(
        PlanRepository planRepository,
        PlanService planService,
        CsvWriter<MassivePlanStatusUpdateCsv> csvWriter)
    {
        this.planRepository = planRepository;
        this.planService = planService;
        this.csvWriter = csvWriter;
    }

    @PutMapping
    public void updatePlanStatuses(HttpServletResponse response) throws IOException
    {
        List<Plan> plans = getPlansToProcess();
        List<MassivePlanStatusUpdateCsv> records = new ArrayList<>();
        int size = plans.size();
        int count = 0;
        Instant init = Instant.now();
        Instant last = Instant.now();
        for (Plan plan : plans)
        {
            System.out.println(plan.getPin());
            String initialStatusName = plan.getStatus().getName();
            planService.updatePlan(plan.getPin(), plan);
            String newStatusName = plan.getStatus().getName();
            count ++;
            if (count % 10 == 0)
            {
                Instant now = Instant.now();
                Duration timeElapsed = Duration.between(last, now);
                System.out.println(count + " of " + size + ". Updated: [" + records.size() + "]. Elapsed since last: " + timeElapsed);
            }

            if (!initialStatusName.equals(newStatusName))
            {
                MassivePlanStatusUpdateCsv record = new MassivePlanStatusUpdateCsv();
                record.setPin(plan.getPin());
                record.setInitialStatus(initialStatusName);
                record.setNewStatus(newStatusName);
                records.add(record);
                System.out.println("--> Changed " + plan.getPin());
            }
            last = Instant.now();
        }
        csvWriter.writeListToCsv(response.getWriter(), records, MassivePlanStatusUpdateCsv.HEADERS);
        Instant end = Instant.now();
        System.out.println("Total: " + Duration.between(init, end));
    }

    private List<Plan> getPlansToProcess()
    {
        List<Plan> plans = new ArrayList<>();

        List<String> filterStatuses =
            Arrays.asList("Plan Created", "Attempt In Progress", "Embryos Obtained");
        plans = planService.getPlans(
            PlanFilterBuilder.getInstance()
                .withStatusNames(filterStatuses)
                .build());

        //plans = Arrays.asList( planRepository.findPlanById(79L));

        plans.sort(Comparator.comparing(Plan::getPin));

        return plans;
    }

    @Data
    static class MassivePlanStatusUpdateCsv implements CsvRecord
    {
        private static final String SEPARATOR = ",";
        private static final String[] HEADERS =
            new String[]
                {
                    "PIN",
                    "Initial Stauts",
                    "New Status"
                };

        private String pin;
        private String initialStatus;
        private String newStatus;

        public String toString()
        {
            return pin + SEPARATOR + initialStatus + SEPARATOR + newStatus;
        }

        @Override
        public String[] getRowAsArray()
        {
            return new String[]
                {
                    pin, initialStatus, newStatus
                };
        }
    }
}
