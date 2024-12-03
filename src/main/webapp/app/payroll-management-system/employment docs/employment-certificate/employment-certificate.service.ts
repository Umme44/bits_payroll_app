import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IEmploymentCertificate } from '../../../shared/legacy/legacy-model/employment-certificate.model';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';
import dayjs from 'dayjs/esm';

type EntityResponseType = HttpResponse<IEmploymentCertificate>;
type EntityArrayResponseType = HttpResponse<IEmploymentCertificate[]>;

@Injectable({ providedIn: 'root' })
export class EmploymentCertificateService {
  public resourceUrl = SERVER_API_URL + 'api/common/employment-certificates';

  constructor(protected http: HttpClient) {}

  create(): Observable<EntityResponseType> {
    const copy = {};
    return this.http
      .post<IEmploymentCertificate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(employmentCertificate: IEmploymentCertificate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentCertificate);
    return this.http
      .put<IEmploymentCertificate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmploymentCertificate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
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

  protected convertDateFromClient(employmentCertificate: IEmploymentCertificate): IEmploymentCertificate {
    const copy: IEmploymentCertificate = Object.assign({}, employmentCertificate, {
      issueDate:
        employmentCertificate.issueDate && employmentCertificate.issueDate.isValid()
          ? employmentCertificate.issueDate.format(DATE_FORMAT)
          : undefined,
      createdAt:
        employmentCertificate.createdAt && employmentCertificate.createdAt.isValid() ? employmentCertificate.createdAt.toJSON() : undefined,
      updatedAt:
        employmentCertificate.updatedAt && employmentCertificate.updatedAt.isValid() ? employmentCertificate.updatedAt.toJSON() : undefined,
      generatedAt:
        employmentCertificate.generatedAt && employmentCertificate.generatedAt.isValid()
          ? employmentCertificate.generatedAt.toJSON()
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
