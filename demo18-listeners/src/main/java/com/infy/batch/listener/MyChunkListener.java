package com.infy.batch.listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

public class MyChunkListener {

	@BeforeChunk
	public void beforeChunk(ChunkContext chunkContext) {
		System.out.println("******ChunkListener - beforeChunk()*******");
	}

	@AfterChunk
	public void afterChunk(ChunkContext chunkContext) {
		System.out.println("******ChunkListener - afterChunk()*******");
		int count = chunkContext.getStepContext().getStepExecution().getReadCount();
	    System.out.println("ItemCount After Chunk: " + count);
	}

	@AfterChunkError
	public void afterChunkError(ChunkContext chunkContext) {
		System.out.println("******ChunkListener - afterChunkError()*******");
	}

}
