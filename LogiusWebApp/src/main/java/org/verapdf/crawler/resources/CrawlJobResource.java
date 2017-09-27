package org.verapdf.crawler.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.verapdf.crawler.api.crawling.CrawlRequest;
import org.verapdf.crawler.core.heritrix.HeritrixClient;
import org.verapdf.crawler.api.crawling.CrawlJob;
import org.verapdf.crawler.db.CrawlJobDAO;
import org.verapdf.crawler.db.CrawlRequestDAO;
import org.verapdf.crawler.tools.DomainUtils;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

@Path("/crawl-jobs")
@Produces(MediaType.APPLICATION_JSON)
public class CrawlJobResource {

    private static final Logger logger = LoggerFactory.getLogger(CrawlJobResource.class);

    private final CrawlJobDAO crawlJobDao;
    private final HeritrixClient heritrix;

    public CrawlJobResource(CrawlJobDAO crawlJobDao, HeritrixClient heritrix) {
        this.crawlJobDao = crawlJobDao;
        this.heritrix = heritrix;
    }

    @GET
    @UnitOfWork
    public Response getJobList(@QueryParam("domainFilter") String domainFilter,
                               @QueryParam("start") IntParam startParam,
                               @QueryParam("limit") IntParam limitParam) {
        Integer start = startParam != null ? startParam.get() : null;
        Integer limit = limitParam != null ? limitParam.get() : null;

        long totalCount = crawlJobDao.count(domainFilter);
        List<CrawlJob> crawlJobs = crawlJobDao.find(domainFilter, start, limit);
        return Response.ok(crawlJobs).header("X-Total-Count", totalCount).build();
    }

    @POST
    @Path("/{domain}")
    public CrawlJob restartCrawlJob(@PathParam("domain") String domain) {
//        //TODO: REWRITE!!!!
//        //TODO: should delete this job and launch a new one with new id
//        try {
//            CrawlJob crawlJob = crawlJobDao.getCrawlJobByCrawlUrl(domain);
//            List<String> list = new ArrayList<>();
//            list.add(crawlJob.getDomain());
//            crawlJobDao.setJobFinished(domain, false);
//            heritrix.teardownJob(domain);
//            heritrix.createJob(domain, list);
//            heritrix.buildJob(domain);
//            heritrix.launchJob(domain);
//            logger.info("Crawl job on "+ crawlJob.getDomain() + " restarted");
//            return getCrawlJob(domain);
//        }
//        catch (Exception e) {
//            logger.error("Error restarting job", e);
            return null;
//        }
    }

    @GET
    @Path("/{domain}")
    @UnitOfWork
    public CrawlJob getCrawlJob(@PathParam("domain") String domain) {
        domain = DomainUtils.trimUrl(domain);

        CrawlJob crawlJob = crawlJobDao.getByDomain(domain);
        if (crawlJob == null) {
            throw new WebApplicationException("Domain " + domain + " not found", Response.Status.NOT_FOUND);
        }
        return crawlJob;
    }

    @PUT
    @Path("/{domain}")
    @UnitOfWork
    public CrawlJob updateCrawlJob(@PathParam("domain") String domain, @NotNull CrawlJob update) throws IOException {
        CrawlJob crawlJob = this.getCrawlJob(domain);

        if(crawlJob.getStatus() == CrawlJob.Status.RUNNING && update.getStatus() == CrawlJob.Status.PAUSED) {
            heritrix.pauseJob(crawlJob.getHeritrixJobId());
            crawlJob.setStatus(CrawlJob.Status.PAUSED);
        }
        if(crawlJob.getStatus() == CrawlJob.Status.PAUSED && update.getStatus() == CrawlJob.Status.RUNNING) {
            heritrix.unpauseJob(crawlJob.getHeritrixJobId());
            crawlJob.setStatus(CrawlJob.Status.RUNNING);
        }
        return crawlJob;
    }

    @GET
    @Path("/{domain}/requests")
    @UnitOfWork
    public List<CrawlRequest> getCrawlJobRequests(@PathParam("domain") String domain) {
        CrawlJob crawlJob = getCrawlJob(domain);
        return crawlJob.getCrawlRequests();
    }

    @DELETE
    @Path("/{domain}/requests")
    @UnitOfWork
    public List<CrawlRequest> unlinkCrawlRequests(@PathParam("domain") String domain, @QueryParam("email") @NotNull String email) {
        CrawlJob crawlJob = getCrawlJob(domain);

        ListIterator<CrawlRequest> crawlRequests = crawlJob.getCrawlRequests().listIterator();
        while(crawlRequests.hasNext()){
            if(email.equals(crawlRequests.next().getEmailAddress())){
                crawlRequests.remove();
            }
        }

//        if (crawlJob.getCrawlRequests().size() == 0) {
//            // todo: clarify if possible/required to terminate CrawlJob if no associated CrawlRequests left
//        }

        return crawlJob.getCrawlRequests();
    }
//
//    @GET
//    @Path("/{domain}/documents")
//    public List<Object> getDomainDocuments(@PathParam("domain") String domain,
//                                           @QueryParam("startDate") DateParam startDate,
//                                           @QueryParam("type") String type,
//                                           @QueryParam("start") IntParam start,
//                                           @QueryParam("limit") IntParam limit,
//                                           @QueryParam("property") List<String> properties) {
//        /* todo: introduce new domain object DomainDocument with the following structure:
//            {
//                url: '',
//                contentType: '',
//                compliant: true,
//                properties: {
//                    requestedProperty1: '',
//                    requestedProperty2: '',
//                    ...
//                },
//                errors: [
//                    'Error description 1',
//                    'Error description 2'
//                ]
//            }
//         */
//        return null;
//    }
}
