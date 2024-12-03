import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { ISalaryCertificate } from '../../model/salary-certificate.model';
import { createRequestOption } from '../../../../core/request/request-util';
import { CertificateApprovalDto } from '../../model/certificate-approval-dto';
import { DATE_FORMAT } from '../../../../config/input.constants';
import { ISalaryCertificateReport } from '../../model/salary-certificate-report.model';

type SalaryCertificateResponseType = HttpResponse<ISalaryCertificate>;
type SalaryCertificateArrayResponseType = HttpResponse<ISalaryCertificate[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeSalaryCertificateAdminService {
  public resourceUrl = SERVER_API_URL + 'api/payroll-mgt/salary-certificates';
  public resourceUrlApprovalHR = SERVER_API_URL + 'api/payroll-mgt/salary-certificate-approval';

  constructor(protected http: HttpClient) {}

  querySalaryCertificate(req?: any): Observable<SalaryCertificateArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISalaryCertificate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: SalaryCertificateArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  find(id: number): Observable<SalaryCertificateResponseType> {
    return this.http
      .get<ISalaryCertificate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: SalaryCertificateResponseType) => this.convertDateFromServer(res)));
  }

  getEmployeeSalaryReportByCertificateId(id: any): Observable<HttpResponse<ISalaryCertificateReport>> {
    return this.http
      .get<ISalaryCertificateReport>(this.resourceUrl + '/salary-certificate-report/' + id, { observe: 'response' })
      .pipe(map((res: HttpResponse<ISalaryCertificateReport>) => this.convertDateFromServerForCertificateReport(res)));
  }

  approveSalaryCertificate(approvalDto: CertificateApprovalDto, id: number): Observable<HttpResponse<boolean>> {
    const copy = Object.assign({}, approvalDto, {
      issueDate: approvalDto.issueDate && approvalDto.issueDate.isValid() ? approvalDto.issueDate.format(DATE_FORMAT) : undefined,
    });
    return this.http.put<boolean>(this.resourceUrlApprovalHR + `/approve/${id}`, copy, {
      observe: 'response',
    });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  isReferenceNumberUnique(referenceNumber: string): Observable<HttpResponse<boolean>> {
    const options = createRequestOption({ referenceNumber });
    return this.http.get<boolean>(this.resourceUrl + `/is-unique`, { params: options, observe: 'response' });
  }
  protected convertDateFromServerForCertificateReport(res: HttpResponse<ISalaryCertificateReport>): HttpResponse<ISalaryCertificateReport> {
    if (res.body) {
      res.body.joiningDate = res.body.joiningDate ? dayjs(res.body.joiningDate) : undefined;
      res.body.confirmationDate = res.body.confirmationDate ? dayjs(res.body.confirmationDate) : undefined;
    }
    return res;
  }

  protected convertDateFromServer(res: SalaryCertificateResponseType): SalaryCertificateResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.sanctionAt = res.body.sanctionAt ? dayjs(res.body.sanctionAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: SalaryCertificateArrayResponseType): SalaryCertificateArrayResponseType {
    if (res.body) {
      res.body.forEach((salaryCertificate: ISalaryCertificate) => {
        salaryCertificate.createdAt = salaryCertificate.createdAt ? dayjs(salaryCertificate.createdAt) : undefined;
        salaryCertificate.updatedAt = salaryCertificate.updatedAt ? dayjs(salaryCertificate.updatedAt) : undefined;
        salaryCertificate.sanctionAt = salaryCertificate.sanctionAt ? dayjs(salaryCertificate.sanctionAt) : undefined;
      });
    }
    return res;
  }
}
