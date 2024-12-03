import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecruitmentRequisitionForm, NewRecruitmentRequisitionForm } from '../recruitment-requisition-form.model';
import { IRrfRaiseValidity } from '../../../shared/model/rrf-raise-validity.model';

export type PartialUpdateRecruitmentRequisitionForm = Partial<IRecruitmentRequisitionForm> & Pick<IRecruitmentRequisitionForm, 'id'>;

type RestOf<T extends IRecruitmentRequisitionForm | NewRecruitmentRequisitionForm> = Omit<
  T,
  | 'expectedJoiningDate'
  | 'dateOfRequisition'
  | 'requestedDate'
  | 'recommendationDate01'
  | 'recommendationDate02'
  | 'recommendationDate03'
  | 'recommendationDate04'
  | 'rejectedAt'
  | 'createdAt'
  | 'updatedAt'
> & {
  expectedJoiningDate?: string | null;
  dateOfRequisition?: string | null;
  requestedDate?: string | null;
  recommendationDate01?: string | null;
  recommendationDate02?: string | null;
  recommendationDate03?: string | null;
  recommendationDate04?: string | null;
  rejectedAt?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestRecruitmentRequisitionForm = RestOf<IRecruitmentRequisitionForm>;

export type NewRestRecruitmentRequisitionForm = RestOf<NewRecruitmentRequisitionForm>;

export type PartialUpdateRestRecruitmentRequisitionForm = RestOf<PartialUpdateRecruitmentRequisitionForm>;

export type EntityResponseType = HttpResponse<IRecruitmentRequisitionForm>;
export type EntityArrayResponseType = HttpResponse<IRecruitmentRequisitionForm[]>;

@Injectable({ providedIn: 'root' })
export class RecruitmentRequisitionFormService {
  public resourceUrl = SERVER_API_URL + 'api/employee-mgt/recruitment-requisition-forms';
  public userResourceUrl = SERVER_API_URL + 'api/common/rrf';
  public onBehalfResourceUrl = SERVER_API_URL + 'api/common/rrf/raise-on-behalf';

  constructor(protected http: HttpClient) {}

  create(recruitmentRequisitionForm: IRecruitmentRequisitionForm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recruitmentRequisitionForm);
    return this.http
      .post<IRecruitmentRequisitionForm>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(recruitmentRequisitionForm: IRecruitmentRequisitionForm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recruitmentRequisitionForm);
    return this.http
      .put<IRecruitmentRequisitionForm>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRecruitmentRequisitionForm>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRecruitmentRequisitionForm[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  raiseOnBehalfCommon(model: IRecruitmentRequisitionForm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(model);
    return this.http
      .post(this.onBehalfResourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateOnBehalfCommon(recruitmentRequisitionForm: IRecruitmentRequisitionForm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recruitmentRequisitionForm);
    return this.http
      .put<IRecruitmentRequisitionForm>(this.onBehalfResourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  queryOnBehalfCommon(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRecruitmentRequisitionForm[]>(this.onBehalfResourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findOnBehalfCommon(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRecruitmentRequisitionForm>(`${this.onBehalfResourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  deleteOnBehalfCommon(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.onBehalfResourceUrl}/${id}`, { observe: 'response' });
  }

  createCommon(model: IRecruitmentRequisitionForm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(model);
    return this.http
      .post(this.userResourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateCommon(recruitmentRequisitionForm: IRecruitmentRequisitionForm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recruitmentRequisitionForm);
    return this.http
      .put<IRecruitmentRequisitionForm>(this.userResourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findCommon(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRecruitmentRequisitionForm>(`${this.userResourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  queryCommon(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRecruitmentRequisitionForm[]>(`${this.userResourceUrl}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  deleteCommon(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.userResourceUrl}/${id}`, { observe: 'response' });
  }

  makeRrfFullyClosed(id: number): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(`${this.resourceUrl}/close-rrf/${id}`, { observe: 'response' });
  }

  makeRrfPartiallyClosed(id: number, totalOnboard: number): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(`${this.resourceUrl}/close-rrf-partially/${id}/${totalOnboard}`, { observe: 'response' });
  }

  protected convertDateFromClient(recruitmentRequisitionForm: IRecruitmentRequisitionForm): IRecruitmentRequisitionForm {
    const copy: IRecruitmentRequisitionForm = Object.assign({}, recruitmentRequisitionForm, {
      expectedJoiningDate:
        recruitmentRequisitionForm.expectedJoiningDate && recruitmentRequisitionForm.expectedJoiningDate.isValid()
          ? recruitmentRequisitionForm.expectedJoiningDate.format(DATE_FORMAT)
          : undefined,
      dateOfRequisition:
        recruitmentRequisitionForm.dateOfRequisition && recruitmentRequisitionForm.dateOfRequisition.isValid()
          ? recruitmentRequisitionForm.dateOfRequisition.format(DATE_FORMAT)
          : undefined,
      requestedDate:
        recruitmentRequisitionForm.requestedDate && recruitmentRequisitionForm.requestedDate.isValid()
          ? recruitmentRequisitionForm.requestedDate.format(DATE_FORMAT)
          : undefined,
      recommendationDate01:
        recruitmentRequisitionForm.recommendationDate01 && recruitmentRequisitionForm.recommendationDate01.isValid()
          ? recruitmentRequisitionForm.recommendationDate01.format(DATE_FORMAT)
          : undefined,
      recommendationDate02:
        recruitmentRequisitionForm.recommendationDate02 && recruitmentRequisitionForm.recommendationDate02.isValid()
          ? recruitmentRequisitionForm.recommendationDate02.format(DATE_FORMAT)
          : undefined,
      recommendationDate03:
        recruitmentRequisitionForm.recommendationDate03 && recruitmentRequisitionForm.recommendationDate03.isValid()
          ? recruitmentRequisitionForm.recommendationDate03.format(DATE_FORMAT)
          : undefined,
      recommendationDate04:
        recruitmentRequisitionForm.recommendationDate04 && recruitmentRequisitionForm.recommendationDate04.isValid()
          ? recruitmentRequisitionForm.recommendationDate04.format(DATE_FORMAT)
          : undefined,
      recommendationDate05:
        recruitmentRequisitionForm.recommendationDate05 && recruitmentRequisitionForm.recommendationDate05.isValid()
          ? recruitmentRequisitionForm.recommendationDate05.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expectedJoiningDate = res.body.expectedJoiningDate ? dayjs(res.body.expectedJoiningDate) : undefined;
      res.body.dateOfRequisition = res.body.dateOfRequisition ? dayjs(res.body.dateOfRequisition) : undefined;
      res.body.requestedDate = res.body.requestedDate ? dayjs(res.body.requestedDate) : undefined;
      res.body.recommendationDate01 = res.body.recommendationDate01 ? dayjs(res.body.recommendationDate01) : undefined;
      res.body.recommendationDate02 = res.body.recommendationDate02 ? dayjs(res.body.recommendationDate02) : undefined;
      res.body.recommendationDate03 = res.body.recommendationDate03 ? dayjs(res.body.recommendationDate03) : undefined;
      res.body.recommendationDate04 = res.body.recommendationDate04 ? dayjs(res.body.recommendationDate04) : undefined;
      res.body.recommendationDate05 = res.body.recommendationDate05 ? dayjs(res.body.recommendationDate05) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((recruitmentRequisitionForm: IRecruitmentRequisitionForm) => {
        recruitmentRequisitionForm.expectedJoiningDate = recruitmentRequisitionForm.expectedJoiningDate
          ? dayjs(recruitmentRequisitionForm.expectedJoiningDate)
          : undefined;
        recruitmentRequisitionForm.dateOfRequisition = recruitmentRequisitionForm.dateOfRequisition
          ? dayjs(recruitmentRequisitionForm.dateOfRequisition)
          : undefined;
        recruitmentRequisitionForm.requestedDate = recruitmentRequisitionForm.requestedDate
          ? dayjs(recruitmentRequisitionForm.requestedDate)
          : undefined;
        recruitmentRequisitionForm.recommendationDate01 = recruitmentRequisitionForm.recommendationDate01
          ? dayjs(recruitmentRequisitionForm.recommendationDate01)
          : undefined;
        recruitmentRequisitionForm.recommendationDate02 = recruitmentRequisitionForm.recommendationDate02
          ? dayjs(recruitmentRequisitionForm.recommendationDate02)
          : undefined;
        recruitmentRequisitionForm.recommendationDate03 = recruitmentRequisitionForm.recommendationDate03
          ? dayjs(recruitmentRequisitionForm.recommendationDate03)
          : undefined;
        recruitmentRequisitionForm.recommendationDate04 = recruitmentRequisitionForm.recommendationDate04
          ? dayjs(recruitmentRequisitionForm.recommendationDate04)
          : undefined;
        recruitmentRequisitionForm.recommendationDate05 = recruitmentRequisitionForm.recommendationDate05
          ? dayjs(recruitmentRequisitionForm.recommendationDate05)
          : undefined;
      });
    }
    return res;
  }

  canRaiseRRF(): Observable<HttpResponse<IRrfRaiseValidity>> {
    return this.http.get<IRrfRaiseValidity>(`${this.userResourceUrl}/can-raise-rrf`, { observe: 'response' });
  }

  public rrfExportDownload(req?: any): Observable<Blob> {
    const options = createRequestOption(req);
    return this.http.get(`${this.resourceUrl}/export`, { responseType: 'blob', params: options });
  }
}
