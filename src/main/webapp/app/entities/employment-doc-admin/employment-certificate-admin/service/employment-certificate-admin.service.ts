import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IEmploymentCertificate } from '../../model/employment-certificate.model';
import { createRequestOption } from '../../../../core/request/request-util';
import { CertificateApprovalDto } from '../../model/certificate-approval-dto';
import { DATE_FORMAT } from '../../../../config/input.constants';

type EntityResponseType = HttpResponse<IEmploymentCertificate>;
type EntityArrayResponseType = HttpResponse<IEmploymentCertificate[]>;

@Injectable({ providedIn: 'root' })
export class EmploymentCertificateAdminService {
  public resourceUrl = SERVER_API_URL + 'api/employee-mgt/employment-certificates';

  constructor(protected http: HttpClient) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmploymentCertificate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findPrintFormatById(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmploymentCertificate>(`${this.resourceUrl}/print-format/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmploymentCertificate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  approveEmploymentCertificate(approvalDto: CertificateApprovalDto, id: number): Observable<EntityResponseType> {
    const copy = Object.assign({}, approvalDto, {
      issueDate: approvalDto.issueDate && approvalDto.issueDate.isValid() ? approvalDto.issueDate.format(DATE_FORMAT) : undefined,
    });
    return this.http
      .post<CertificateApprovalDto>(this.resourceUrl + `/approve/${id}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  // rejectEmploymentCertificate(approvalDto: CertificateApprovalDto, id: number): Observable<EntityResponseType> {
  //   const copy = Object.assign({}, approvalDto);
  //   return this.http
  //     .post<CertificateApprovalDto>(this.resourceUrl + `/reject/${id}`, copy, { observe: 'response' })
  //     .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  // }

  isReferenceNumberUnique(referenceNumber: string): Observable<HttpResponse<boolean>> {
    const options = createRequestOption({ referenceNumber });
    return this.http.get<boolean>(this.resourceUrl + `/is-unique`, { params: options, observe: 'response' });
  }

  protected convertDateFromClient(employmentCertificate: IEmploymentCertificate): IEmploymentCertificate {
    const copy: IEmploymentCertificate = Object.assign({}, employmentCertificate, {
      issueDate:
        employmentCertificate.issueDate && employmentCertificate.issueDate.isValid()
          ? employmentCertificate.issueDate.format(DATE_FORMAT)
          : undefined,
      createdAt:
        employmentCertificate.createdAt && employmentCertificate.createdAt.isValid()
          ? employmentCertificate.createdAt.format(DATE_FORMAT)
          : undefined,
      updatedAt:
        employmentCertificate.updatedAt && employmentCertificate.updatedAt.isValid()
          ? employmentCertificate.updatedAt.format(DATE_FORMAT)
          : undefined,
      generatedAt:
        employmentCertificate.generatedAt && employmentCertificate.generatedAt.isValid()
          ? employmentCertificate.generatedAt.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.issueDate = res.body.issueDate ? dayjs(res.body.issueDate) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.generatedAt = res.body.generatedAt ? dayjs(res.body.generatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employmentCertificate: IEmploymentCertificate) => {
        employmentCertificate.issueDate = employmentCertificate.issueDate ? dayjs(employmentCertificate.issueDate) : undefined;
        employmentCertificate.createdAt = employmentCertificate.createdAt ? dayjs(employmentCertificate.createdAt) : undefined;
        employmentCertificate.updatedAt = employmentCertificate.updatedAt ? dayjs(employmentCertificate.updatedAt) : undefined;
        employmentCertificate.generatedAt = employmentCertificate.generatedAt ? dayjs(employmentCertificate.generatedAt) : undefined;
      });
    }
    return res;
  }
}
